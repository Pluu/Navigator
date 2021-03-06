package com.pluu.navigator

import androidx.annotation.VisibleForTesting
import androidx.collection.ArrayMap
import androidx.collection.SparseArrayCompat
import androidx.collection.contains
import androidx.collection.valueIterator
import com.pluu.navigator.deeplink.DeepLinkMatchResult
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

    fun addDestination(
        destination: Destination,
        creator: CREATOR_ACTION
    ) = addDestination(destination, creator.toRouting())

    fun addDeepLink(
        destination: Destination,
        executor: AbstractExecutor
    ): Routing? {
        if (containsDestination(destination)) {
            logger.w("[$name] DeepLink ${destination.path}(${destination.hashCode()}) is already registered ")
            return null
        }

        val path = createDeepLinkPath(destination.path)
        val completedDestination = destination.takeIf {
            it.path.hasScheme()
        } ?: let {
            logger.d("[$name] Migration ${destination.path} to $path ")
            DeepLink(path)
        }

        addDeepLink(NavDeepLink(path))
        return addDestination(completedDestination, executor)
    }

    internal fun addDestination(
        destination: Destination,
        routing: Routing
    ): Routing? {
        if (containsDestination(destination)) {
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
            logger.w("[$name] DeepLink ('${deepLink.uriPattern}') is already registered ")
            return
        }
        deepLinks.put(deepLink.hashCode(), deepLink)
        logger.d("[$name] Added deeplink ${deepLink.uriPattern}")
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
            addDestination(destination, routing)
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

    fun containsDestination(destination: Destination): Boolean {
        return routingNodes.contains(destination)
    }

    fun getRequiredRouting(destination: Destination): Routing {
        return routingNodes[destination]!!
    }

    ///////////////////////////////////////////////////////////////////////////
    // DeepLink
    ///////////////////////////////////////////////////////////////////////////

    fun matchDeepLink(request: DeepLinkRequest): DeepLinkMatchResult? {
        val uri = request.uri.toString().trimUriSeparator()
        for (deepLink in deepLinks.valueIterator()) {
            if (deepLink.match(uri)) {
                val destination = findDestination(deepLink.uriPattern)
                return if (destination != null) {
                    DeepLinkMatchResult(
                        request = request,
                        destination = destination,
                        args = deepLink.matchingArguments(uri).orEmpty()
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

    @VisibleForTesting
    fun clear() {
        routingNodes.clear()
        deepLinks.clear()
    }

    class Builder(
        private val graphName: String,
        private val deepLinkConfig: DeepLinkConfig? = null
    ) {
        private val routeList = mutableMapOf<Destination, CREATOR_ACTION>()
        private val deepLinkList = mutableMapOf<String, AbstractExecutor>()
        private val graphList = mutableListOf<RouteGraph>()

        fun addDestination(
            destination: Destination,
            creator: CREATOR_ACTION
        ) = apply {
            routeList[destination] = creator
        }

        fun addDeepLink(
            path: String,
            executor: EXECUTOR_ACTION
        ) = apply {
            addDeepLink(path, executor.toRouting())
        }

        fun addDeepLink(
            path: String,
            executor: AbstractExecutor
        ) = apply {
            deepLinkList[path] = executor
        }

        fun <T : DeepLinkCommand> addDeepLink(
            path: String,
            deepLinkCommand: Class<T>
        ) = apply {
            addDeepLink(path, CommandRouting(deepLinkCommand))
        }

        fun addGraph(routeGraph: RouteGraph) = apply {
            graphList.add(routeGraph)
        }

        fun build(): RouteGraph {
            return RouteGraph(graphName).apply {
                if (deepLinkConfig != null) {
                    setPath(deepLinkConfig.prefixPath)
                }
                for ((destination, creator) in routeList) {
                    this.addDestination(destination, creator.toRouting())
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
