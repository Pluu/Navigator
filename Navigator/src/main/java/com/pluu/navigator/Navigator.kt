package com.pluu.navigator

import android.app.Activity
import androidx.fragment.app.Fragment
import com.pluu.navigator.starter.NavigatorStarter
import com.pluu.starter.ActivityStarter
import com.pluu.starter.FragmentStarter

object Navigator {

    private val coreGraph = RouteGraph()
    private val deepLinkExecutor = DeepLinkExecutor()

    internal fun addDestination(
        route: Destination,
        creator: INTENT_CREATOR
    ) {
       coreGraph.addRoute(route, creator)
    }

    internal fun addDestination(
        route: Destination,
        executor: LINK_EXECUTOR
    ) {
        addDestinationWithExecutor(route, executor.toRouting())
    }

    internal fun addDestinationWithExecutor(
        route: Destination,
        executor: AbstractExecutor
    ) {
        coreGraph.addDeepLink(route, executor)
    }

    fun addDestinations(graph: RouteGraph) {
        coreGraph.addRouteGraph(graph)
    }

    fun of(activity: Activity) = NavigatorStarter(
        ActivityStarter(activity),
        coreGraph,
        deepLinkExecutor
    )

    fun of(fragment: Fragment) = NavigatorStarter(
        FragmentStarter(fragment),
        coreGraph,
        deepLinkExecutor
    )

    fun registerConfig(config: NavigatorController.Config) {
        NavigatorController.setConfig(config)
    }
}