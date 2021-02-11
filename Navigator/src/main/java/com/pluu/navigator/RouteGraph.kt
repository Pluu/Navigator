package com.pluu.navigator

import androidx.collection.ArrayMap
import androidx.collection.SparseArrayCompat
import androidx.collection.contains
import androidx.collection.valueIterator
import com.pluu.navigator.deeplink.DeepLinkMatch
import com.pluu.navigator.deeplink.DeepLinkRequest
import com.pluu.navigator.deeplink.NavDeepLink
import com.pluu.navigator.util.hasScheme
import com.pluu.navigator.util.toIteratorWithRemove
import com.pluu.navigator.util.toRouting
import com.pluu.navigator.util.trimUriSeparator

class RouteGraph internal constructor(
    private val name: String = "Graph"
) : Destination() {
    private val routingNodes = ArrayMap<Destination, Routing>()
    private val deepLinks = SparseArrayCompat<NavDeepLink>()

    ///////////////////////////////////////////////////////////////////////////
    // Add Destination
    ///////////////////////////////////////////////////////////////////////////

    fun addRoute(
        destination: Destination,
        creator: INTENT_CREATOR
    ) = addRoute(destination, CreateRoutingImpl(creator))

    fun addDeepLink(
        destination: Destination,
        executor: AbstractExecutor
    ): Routing? {
        if (containsRoute(destination)) {
            logger.w("[$name] DeepLink ${destination.path}(${destination.hashCode()}) is already registered ")
            return null
        }

        val path = createDeepLinkPath(destination.path)
        val completedDestination = destination.takeIf {
            it.path.hasScheme()
        } ?: let {
            logger.d("[$name] Migration ${destination.path} to ${path} ")
            DeepLink(path)
        }

        addDeepLink(NavDeepLink(path))
        return addRoute(completedDestination, executor)
    }

    internal fun addRoute(
        destination: Destination,
        routing: Routing
    ): Routing? {
        if (containsRoute(destination)) {
            logger.w("[$name] Route ('${destination.path}') is already registered ")
            return null
        }
        logger.d("[$name] Added routing ${destination.path}")
        return routingNodes.put(destination, routing)
    }

    private fun addDeepLink(
        deepLink: NavDeepLink
    ) {
        if (deepLinks.contains(deepLink.hashCode())) {
            logger.w("[$name] DeepLink ('${deepLink.uri}') is already registered ")
            return
        }
        deepLinks.put(deepLink.hashCode(), deepLink)
        logger.d("[$name] Added deeplink ${deepLink.uri}")
    }

    fun addRouteGraph(
        routeGraph: RouteGraph
    ) {
        logger.d("from [${routeGraph.name}] to [$name]")
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
        path: String,
        deepLinkConfig: DeepLinkConfig? = null,
    ) = buildString {
        if (path.hasScheme()) {
            append(path.trimUriSeparator())
        } else {
            val baseUrl = deepLinkConfig?.prefixPath.orEmpty()
            if (!baseUrl.hasScheme()) {
                append(NavigatorController.config.baseScheme)
                append("://")
            }

            val trimBaseUrl = baseUrl.trimUriSeparator()
            append(trimBaseUrl)
            val cleanPath = path.trimUriSeparator()
            if (cleanPath.isNotEmpty()) {
                if (trimBaseUrl.isNotEmpty()) {
                    append("/")
                }
                append(cleanPath)
            }
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
        val uri = request.uri.toString().trimUriSeparator()
        for (deepLink in deepLinks.valueIterator()) {
            if (deepLink.match(uri)) {
                val destination = findDestination(deepLink.uri)
                return if (destination != null) {
                    DeepLinkMatch(
                        request = request,
                        destination = destination,
                        args = deepLink.matchingArguments(uri).orEmpty(),
                        isExactDeepLink = deepLink.isExactDeepLink()
                    )
                } else {
                    logger.w("no destination found for deeplink('$uri')")
                    null
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
        private val graphName: String,
        private val deepLinkConfig: DeepLinkConfig? = null
    ) {
        private val routeList = mutableMapOf<Destination, INTENT_CREATOR>()
        private val deepLinkList = mutableMapOf<String, AbstractExecutor>()
        private val graphList = mutableListOf<RouteGraph>()

        fun addRoute(
            route: Destination,
            creator: INTENT_CREATOR
        ) {
            routeList[route] = creator
        }

        fun addDeepLink(
            path: String,
            executor: LINK_EXECUTOR
        ) {
            addDeepLink(path, executor.toRouting())
        }

        fun addDeepLink(
            path: String,
            executor: AbstractExecutor
        ) {
            deepLinkList[path] = executor
        }

        fun <T : DeepLinkCommand> addDeepLink(
            path: String,
            deepLinkCommand: Class<T>
        ) {
            addDeepLink(path, CommandRouting(deepLinkCommand))
        }

        fun addGraph(routeGraph: RouteGraph) {
            graphList.add(routeGraph)
        }

        fun build(): RouteGraph {
            return RouteGraph(graphName).apply {
                if (deepLinkConfig != null) {
                    setPath(deepLinkConfig.prefixPath)
                }
                for ((destination, creator) in routeList) {
                    this.addRoute(destination, CreateRoutingImpl(creator))
                }
                for ((path, executor) in deepLinkList) {
                    this.addDeepLink(
                        DeepLink(this.createDeepLinkPath(path, deepLinkConfig)),
                        executor
                    )
                }
                for (graph in graphList) {
                    this.addRouteGraph(graph)
                }
            }
        }
    }

    fun register() {
        Navigator.addDestinations(this)
    }
}

fun routeGraph(
    graphName: String,
    deepLinkConfig: DeepLinkConfig,
    generator: RouteGraph.Builder.() -> Unit
): RouteGraph {
    val builder = RouteGraph.Builder(graphName, deepLinkConfig)
    builder.generator()
    return builder.build()
}

data class DeepLinkConfig(
    val prefixPath: String
)
