package com.pluu.navigator

import android.app.Activity
import androidx.fragment.app.Fragment
import com.pluu.navigator.exception.AlreadyRegisteredException
import com.pluu.navigator.starter.ActivityStarter
import com.pluu.navigator.starter.FragmentStarter
import com.pluu.navigator.starter.NavigatorStarter

object Navigator {
    private val routingProvider = RoutingProviderImpl()

    fun registerRoute(route: Destination, creator: INTENT_CREATOR) {
        if (routingProvider.containsRoute(route)) {
            throw AlreadyRegisteredException(route.toString())
        } else {
            routingProvider.addRouting(route, creator)
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