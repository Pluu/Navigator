package com.pluu.navigator

import androidx.collection.ArrayMap
import androidx.collection.SparseArrayCompat
import androidx.collection.valueIterator
import com.pluu.navigator.deeplink.DeepLink
import com.pluu.navigator.deeplink.DeepLinkMatch
import com.pluu.navigator.deeplink.DeepLinkRequest
import com.pluu.navigator.deeplink.NavDeepLink
import com.pluu.navigator.exception.MissingThrowable
import com.pluu.navigator.util.hasScheme
import com.pluu.navigator.util.toIteratorWithRemove

class RouteGraph internal constructor() : Destination() {
    private val routingNodes = ArrayMap<Destination, Routing>()
    private val deepLinks = SparseArrayCompat<NavDeepLink>()

    ///////////////////////////////////////////////////////////////////////////
    // Add Destination
    ///////////////////////////////////////////////////////////////////////////

    fun addRoute(
        destination: Destination,
        creator: INTENT_CREATOR
    ) = routingNodes.put(destination, CreateRoutingImpl(creator))

    fun addDeepLink(
        destination: Destination,
        executor: LINK_EXECUTOR
    ): Routing? {
        addDeepLink(
            NavDeepLink(
                createDeepLinkPath(
                    NavigatorController.config.deepLinkConfig,
                    destination.path
                )
            )
        )
        return routingNodes.put(destination, ExecuteRoutingImpl(executor))
    }

    fun addRoute(
        destination: Destination,
        routing: Routing
    ) = routingNodes.put(destination, routing)

    private fun addDeepLink(
        deepLink: NavDeepLink
    ) {
        deepLinks.put(deepLink.hashCode(), deepLink)
    }

    fun addRouteGraph(
        routeGraph: RouteGraph
    ) {
        // Add, Routing
        val routeIterator = routeGraph.routeIterator()
        while (routeIterator.hasNext()) {
            val (destination, routing) = routeIterator.next()
            routeIterator.remove()
            addRoute(destination, routing)
        }

        // Add, Deep Link
        val deepLinkIterator = routeGraph.deepLinkIterator()
        while (deepLinkIterator.hasNext()) {
            val deepLink = deepLinkIterator.next()
            deepLinkIterator.remove()
            addDeepLink(deepLink)
        }
    }

    internal fun createDeepLinkPath(
        deepLinkConfig: DeepLinkConfig? = null,
        path: String
    ) = buildString {
        if (path.hasScheme()) {
            append(path)
        } else {
            val baseUrl = deepLinkConfig?.path.orEmpty()
            val separatorIdxBaseUrl = if (baseUrl.last() == '/') {
                -1
            } else {
                0
            }
            append(baseUrl.substring(0, baseUrl.length + separatorIdxBaseUrl))
            if (!baseUrl.hasScheme()) {
                append("://")
            }
            val excludeSeparatorIndex = if (path.first() == '/') {
                1
            } else {
                0
            }
            append(path.substring(startIndex = excludeSeparatorIndex))
        }
    }

    private fun routeIterator() = routingNodes.toIteratorWithRemove()

    private fun deepLinkIterator() = deepLinks.toIteratorWithRemove()

    fun containsRoute(destination: Destination): Boolean {
        return routingNodes.contains(destination)
    }

    fun getRequiredRouting(destination: Destination): Routing {
        return routingNodes[destination]!!
    }

    ///////////////////////////////////////////////////////////////////////////
    // DeepLink
    ///////////////////////////////////////////////////////////////////////////

    fun matchDeepLink(request: DeepLinkRequest): DeepLinkMatch? {
        for (deepLink in deepLinks.valueIterator()) {
            val uri = request.uri
            if (deepLink.match(uri.toString())) {
                val destination = findDestination(deepLink.uri)
                if (destination != null) {
                    return DeepLinkMatch(
                        request = request,
                        destination = destination,
                        args = deepLink.matchingArguments(uri).orEmpty(),
                        isExactDeepLink = deepLink.isExactDeepLink()
                    )
                } else {
                    throw MissingThrowable("destination", "deeplink")
                }
            }
        }
        return null
    }

    private fun findDestination(path: String): Destination? {
        for ((destination, _) in routingNodes) {
            if (destination.path == path) {
                return destination
            }
        }
        return null
    }

    class Builder(
        private val deepLinkConfig: DeepLinkConfig? = null
    ) {
        private val routeList = mutableListOf<Pair<Destination, INTENT_CREATOR>>()
        private val deepLinkList = mutableListOf<Pair<String, LINK_EXECUTOR>>()

        fun addRoute(
            route: Destination,
            creator: INTENT_CREATOR
        ) {
            routeList.add(route to creator)
        }

        fun addDeepLink(
            path: String,
            executor: LINK_EXECUTOR
        ) {
            deepLinkList.add(path to executor)
        }

        fun build(): RouteGraph {
            return RouteGraph().apply {
                if (deepLinkConfig != null) {
                    setPath(deepLinkConfig.path)
                }
                for ((destination, creator) in routeList) {
                    this.addRoute(destination, CreateRoutingImpl(creator))
                }
                for ((path, executor) in deepLinkList) {
                    this.addDeepLink(
                        DeepLink(this.createDeepLinkPath(deepLinkConfig, path)),
                        executor
                    )
                }
            }
        }
    }
}

fun routeGraph(
    deepLinkConfig: DeepLinkConfig,
    generator: RouteGraph.Builder.() -> Unit
): RouteGraph {
    val builder = RouteGraph.Builder(deepLinkConfig)
    builder.generator()
    return builder.build()
}

data class DeepLinkConfig(
    val path: String
)
