package com.pluu.navigator.starter

import android.content.Intent
import androidx.core.net.toUri
import com.pluu.navigator.*
import com.pluu.navigator.deeplink.DeepLinkRequest
import com.pluu.navigator.exception.MissingRouteThrowable
import com.pluu.starter.Starter

class NavigatorStarter(
    private val starter: Starter,
    private val routingProvider: RoutingProvider
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
        val containRoute = routingProvider.containsRoute(destination)
        if (!containRoute) {
            throw MissingRouteThrowable(routeName = destination.toString())
        }
        starter.context ?: return

        val routing = routingProvider.getRequiredRouting(destination) as? CreateRouting ?: return
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
        val deepLinkMatch = routingProvider.matchDeepLink(request) ?: return false
        val routing = routingProvider.getRequiredRouting(
            deepLinkMatch.destination
        ) as? ExecuteRouting ?: return false
        logger.d("matched deeplink ${request.uri}")
        routing.execute(starter, deepLinkMatch)
        return true
    }
}