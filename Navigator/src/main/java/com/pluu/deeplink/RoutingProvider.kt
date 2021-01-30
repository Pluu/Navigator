package com.pluu.deeplink

class RoutingProvider {
    private val routing = mutableMapOf<AbstractRoute, Routing>()
    private val deepLinks = mutableMapOf<AbstractRoute, NavDeepLink>()

    fun containsRoute(
        destination: AbstractRoute
    ) = routing.contains(destination)

    fun getRequiredRouting(
        destination: AbstractRoute
    ) = routing[destination]

    fun addRouting(
        destination: AbstractRoute,
        creator: LINK_EXECUTOR
    ): Routing? {
        deepLinks[destination] = NavDeepLink(destination.path)
        return routing.put(destination, RoutingImpl(creator))
    }

    fun matchDeepLink(request: DeepLinkRequest): DeepLinkMatch? {
        for ((destination, deepLink) in deepLinks) {
            val uri = request.uri
            return DeepLinkMatch(
                destination,
                deepLink.matchingArguments(uri).orEmpty()
            )
        }
        return null
    }
}