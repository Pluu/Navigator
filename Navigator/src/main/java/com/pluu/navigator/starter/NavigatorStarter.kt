package com.pluu.navigator.starter

import android.content.Intent
import androidx.core.net.toUri
import com.pluu.navigator.*
import com.pluu.navigator.deeplink.DeepLinkRequest
import com.pluu.navigator.exception.MissingRouteThrowable

class NavigatorStarter(
    private val starter: Starter,
    private val graph: RouteGraph,
    private val deepLinkExecutor: DeepLinkExecutor
) {
    ///////////////////////////////////////////////////////////////////////////
    // Navigation
    ///////////////////////////////////////////////////////////////////////////

    fun <T : Route> start(
        route: T,
        navOption: NavOptions? = null,
    ) {
        startInternal(
            destination = route,
            navOption = navOption
        )
    }

    fun <P : RouteParam, T : RouteWithParam<P>> start(
        route: T,
        param: P,
        navOption: NavOptions? = null,
    ) {
        startInternal(
            destination = route,
            param = param,
            navOption = navOption
        )
    }

    fun startForResult(
        route: Route,
        requestCode: Int,
        navOption: NavOptions? = null
    ) {
        startInternal(
            destination = route,
            requestCode = requestCode,
            navOption = navOption
        )
    }

    fun <P : RouteParam, T : RouteWithParam<P>> startForResult(
        route: Route,
        requestCode: Int,
        param: P,
        navOption: NavOptions? = null
    ) {
        startInternal(
            destination = route,
            requestCode = requestCode,
            param = param,
            navOption = navOption
        )
    }

    private fun startInternal(
        destination: Destination,
        requestCode: Int? = null,
        param: RouteParam? = null,
        navOption: NavOptions?
    ) {
        val containRoute = graph.containsRoute(destination)
        if (!containRoute) {
            throw MissingRouteThrowable(routeName = destination.toString())
        }
        starter.context ?: return

        val routing = graph.getRequiredRouting(destination) as? CreateRouting ?: return
        logger.d("matched route ${destination.path}")

        val intent = routing.create(starter)
        if (param != null) {
            intent.putExtra(ROUTE_PARAMS_KEY, param)
        }

        if (navOption != null) {
            if (navOption.singleTop) {
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
        }

        if (requestCode != null) {
            starter.startForResult(intent, requestCode)
        } else {
            starter.start(intent)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // DeepLink
    ///////////////////////////////////////////////////////////////////////////

    fun execute(
        path: String
    ): Boolean {
        return execute(DeepLinkRequest(path.toUri()))
    }

    fun execute(request: DeepLinkRequest): Boolean {
        val deepLinkMatch = graph.matchDeepLink(request) ?: return false
        val routing = graph.getRequiredRouting(
            deepLinkMatch.destination
        ) as? AbstractExecutor ?: return false
        logger.d("matched deeplink ${request.uri}")
        return deepLinkExecutor.execute(routing, starter, deepLinkMatch)
    }
}
