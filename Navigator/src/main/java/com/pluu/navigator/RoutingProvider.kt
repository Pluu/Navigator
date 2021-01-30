package com.pluu.navigator

interface RoutingProvider {
    fun containsRoute(destination: Destination): Boolean

    fun getRequiredRouting(destination: Destination): Routing
}

class RoutingProviderImpl : RoutingProvider {
    private val routing = mutableMapOf<Destination, Routing>()

    fun addRouting(
        destination: Destination,
        creator: INTENT_CREATOR
    ) = routing.put(destination, RoutingImpl(creator))

    override fun containsRoute(destination: Destination): Boolean {
        return routing.contains(destination)
    }

    override fun getRequiredRouting(destination: Destination): Routing {
        return routing[destination]!!
    }
}