package com.pluu.deeplink

import android.app.Activity
import androidx.fragment.app.Fragment
import com.pluu.deeplink.starter.DeepLinkStarter
import com.pluu.exception.AlreadyRegisteredException
import com.pluu.starter.ActivityStarter
import com.pluu.starter.FragmentStarter

object DeepLinker {

    private val routingProvider = RoutingProvider()

    internal fun registerRoute(
        route: AbstractRoute,
        executor: LINK_EXECUTOR
    ) {
        if (routingProvider.containsRoute(route)) {
            throw AlreadyRegisteredException(route.toString())
        } else {
            routingProvider.addRouting(route, executor)
        }
    }

    fun of(activity: Activity) = DeepLinkStarter(
        ActivityStarter(activity),
        routingProvider
    )

    fun of(fragment: Fragment) = DeepLinkStarter(
        FragmentStarter(fragment),
        routingProvider
    )
}