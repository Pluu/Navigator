package com.pluu.navigator

interface AbstractRoute : Destination {
    fun register(creator: INTENT_CREATOR) {
        Navigator.registerRoute(this, creator)
    }
}

abstract class Route : AbstractRoute

abstract class RouteWithParam : AbstractRoute
