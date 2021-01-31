package com.pluu.navigator

import com.pluu.navigator.util.enclosingClassFullSimpleName

abstract class AbstractRoute : Destination {
    // Default : Class Path
    override val path = javaClass.enclosingClassFullSimpleName()

    fun register(creator: INTENT_CREATOR) {
        Navigator.registerRoute(this, creator)
    }

    fun register(executor: LINK_EXECUTOR) {
        Navigator.registerRoute(this, executor)
    }
}

abstract class Route : AbstractRoute()

abstract class RouteWithParam : AbstractRoute()
