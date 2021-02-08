package com.pluu.navigator

const val ROUTE_PARAMS_KEY = "ROUTE_PARAMS_KEY"

abstract class AbstractRoute : Destination() {
    fun register(creator: INTENT_CREATOR) {
        Navigator.addDestination(this, creator)
    }

    fun register(executor: LINK_EXECUTOR) {
        Navigator.addDestination(this, executor)
    }

    fun register(executor: AbstractExecutor) {
        Navigator.addDestinationWithExecutor(this, executor)
    }
}

abstract class Route : AbstractRoute()

abstract class RouteWithParam<T : RouteParam> : AbstractRoute()
