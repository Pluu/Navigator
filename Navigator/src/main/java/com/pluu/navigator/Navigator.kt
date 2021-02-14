package com.pluu.navigator

import android.app.Activity
import androidx.fragment.app.Fragment
import com.pluu.navigator.starter.ActivityStarter
import com.pluu.navigator.starter.FragmentStarter
import com.pluu.navigator.starter.NavigatorStarter
import com.pluu.navigator.util.toRouting

object Navigator {

    private val coreGraph = RouteGraph()
    private val deepLinkExecutor = DeepLinkExecutor()

    internal fun addDestination(
        destination: Destination,
        creator: CREATOR_ACTION
    ) {
       coreGraph.addDestination(destination, creator)
    }

    internal fun addDestination(
        destination: Destination,
        executor: EXECUTOR_ACTION
    ) {
        addDestinationWithExecutor(destination, executor.toRouting())
    }

    internal fun addDestinationWithExecutor(
        destination: Destination,
        executor: AbstractExecutor
    ) {
        coreGraph.addDeepLink(destination, executor)
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