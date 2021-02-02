package com.pluu.navigator

import android.app.Activity
import androidx.fragment.app.Fragment
import com.pluu.navigator.exception.AlreadyRegisteredException
import com.pluu.navigator.starter.NavigatorStarter
import com.pluu.starter.ActivityStarter
import com.pluu.starter.FragmentStarter

object Navigator {

    private val routingProvider = RoutingProviderImpl()

    internal fun registerRoute(
        route: Destination,
        creator: INTENT_CREATOR
    ) {
        if (routingProvider.containsRoute(route)) {
            throw AlreadyRegisteredException(route.toString())
        } else {
            routingProvider.addRouting(route, creator)
        }
    }

    internal fun registerRoute(
        route: Destination,
        executor: LINK_EXECUTOR
    ) {
        if (routingProvider.containsRoute(route)) {
            throw AlreadyRegisteredException(route.toString())
        } else {
            routingProvider.addDeepLink(route, executor)
        }
    }

    fun of(activity: Activity) = NavigatorStarter(
        ActivityStarter(activity),
        routingProvider
    )

    fun of(fragment: Fragment) = NavigatorStarter(
        FragmentStarter(fragment),
        routingProvider
    )
}