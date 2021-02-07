package com.pluu.navigator

import android.app.Activity
import androidx.fragment.app.Fragment
import com.pluu.navigator.exception.AlreadyRegisteredException
import com.pluu.navigator.starter.NavigatorStarter
import com.pluu.starter.ActivityStarter
import com.pluu.starter.FragmentStarter

object Navigator {

    private val coreGraph = RouteGraph()

    internal fun addDestination(
        route: Destination,
        creator: INTENT_CREATOR
    ) {
        if (coreGraph.containsRoute(route)) {
            throw AlreadyRegisteredException(route.toString())
        } else {
            coreGraph.addRoute(route, creator)
            logger.d("Added routing ${route.path}")
        }
    }

    internal fun addDestination(
        route: Destination,
        executor: LINK_EXECUTOR
    ) {
        if (coreGraph.containsRoute(route)) {
            throw AlreadyRegisteredException(route.toString())
        } else {
            coreGraph.addDeepLink(route, executor)
            logger.d("Added deeplink ${route.path}")
        }
    }

    fun addDestinations(graph: RouteGraph) {
        coreGraph.addRouteGraph(graph)
    }

    fun of(activity: Activity) = NavigatorStarter(
        ActivityStarter(activity),
        coreGraph
    )

    fun of(fragment: Fragment) = NavigatorStarter(
        FragmentStarter(fragment),
        coreGraph
    )

    fun registerConfig(config: NavigatorController.Config) {
        NavigatorController.setConfig(config)
    }
}