package com.pluu.navigator

import com.pluu.navigator.deeplink.DeepLinkMatch
import com.pluu.navigator.deeplink.DeepLinkRequest
import com.pluu.navigator.deeplink.NavDeepLink
import com.pluu.navigator.exception.MissingThrowable

interface RoutingProvider {
    fun containsRoute(destination: Destination): Boolean

    fun getRequiredRouting(destination: Destination): Routing

    fun matchDeepLink(request: DeepLinkRequest): DeepLinkMatch?
}

internal class RoutingProviderImpl : RoutingProvider {
    private val routing = mutableMapOf<Destination, Routing>()
    private val deepLinks = mutableListOf<NavDeepLink>()

    fun addRouting(
        destination: Destination,
        creator: INTENT_CREATOR
    ) = routing.put(destination, CreateRoutingImpl(creator))

    fun addDeepLink(
        destination: Destination,
        creator: LINK_EXECUTOR
    ): Routing? {
        deepLinks.add(NavDeepLink(destination.path))
        return this.routing.put(destination, ExecuteRoutingImpl(creator))
    }

    override fun containsRoute(destination: Destination): Boolean {
        return routing.contains(destination)
    }

    override fun getRequiredRouting(destination: Destination): Routing {
        return routing[destination]!!
    }

    override fun matchDeepLink(request: DeepLinkRequest): DeepLinkMatch? {
        for (deepLink in deepLinks) {
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
        for ((destination, _) in routing) {
            if (destination.path == path) {
                return destination
            }
        }
        return null
    }
}