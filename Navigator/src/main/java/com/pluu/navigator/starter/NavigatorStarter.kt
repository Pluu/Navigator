package com.pluu.navigator.starter

import android.content.Intent
import androidx.core.net.toUri
import androidx.core.os.bundleOf
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

    fun <T : RouteWithParam> start(
        route: T,
        args: List<Pair<String, Any?>>? = null,
        navOption: NavOptions? = null,
    ) {
        startInternal(
            destination = route,
            args = args,
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

    fun <T : RouteWithParam> startForResult(
        route: Route,
        requestCode: Int,
        args: List<Pair<String, Any?>>? = null,
        navOption: NavOptions? = null
    ) {
        startInternal(
            destination = route,
            requestCode = requestCode,
            args = args,
            navOption = navOption
        )
    }

    private fun startInternal(
        destination: Destination,
        requestCode: Int? = null,
        args: List<Pair<String, Any?>>? = null,
        navOption: NavOptions?
    ) {
        val containRoute = routingProvider.containsRoute(destination)
        if (!containRoute) {
            throw MissingRouteThrowable(routeName = destination.toString())
        }
        starter.context ?: return

        val routing = routingProvider.getRequiredRouting(destination) as? CreateRouting ?: return

        val intent = routing.create(starter)
        if (args != null) {
            intent.putExtras(bundleOf(*args.toTypedArray()))
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
        routing.execute(starter, deepLinkMatch)
        return true
    }
}