package com.pluu.navigator.starter

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.VisibleForTesting
import androidx.core.net.toUri
import com.pluu.navigator.AbstractExecutor
import com.pluu.navigator.CreateRouting
import com.pluu.navigator.DIRECTION_PARAMS_KEY
import com.pluu.navigator.DeepLinkExecutor
import com.pluu.navigator.Destination
import com.pluu.navigator.Direction
import com.pluu.navigator.DirectionParam
import com.pluu.navigator.DirectionWithParam
import com.pluu.navigator.NavOptions
import com.pluu.navigator.RouteGraph
import com.pluu.navigator.deeplink.DeepLinkRequest
import com.pluu.navigator.exception.MissingRouteThrowable
import com.pluu.navigator.logger

class NavigatorStarter(
    @VisibleForTesting val starter: Starter,
    private val graph: RouteGraph,
    private val deepLinkExecutor: DeepLinkExecutor
) {
    ///////////////////////////////////////////////////////////////////////////
    // Navigation
    ///////////////////////////////////////////////////////////////////////////

    fun <T : Direction> start(
        direction: T,
        navOption: NavOptions? = null,
    ) {
        startInternal(
            destination = direction,
            navOption = navOption
        )
    }

    fun <P : DirectionParam, T : DirectionWithParam<P>> start(
        direction: T,
        param: P,
        navOption: NavOptions? = null,
    ) {
        startInternal(
            destination = direction,
            param = param,
            navOption = navOption
        )
    }

    fun <T : Direction> startForResult(
        direction: T,
        requestCode: Int,
        navOption: NavOptions? = null
    ) {
        startInternal(
            destination = direction,
            requestCode = requestCode,
            navOption = navOption
        )
    }

    fun <P : DirectionParam, T : DirectionWithParam<P>> startForResult(
        direction: T,
        requestCode: Int,
        param: P,
        navOption: NavOptions? = null
    ) {
        startInternal(
            destination = direction,
            requestCode = requestCode,
            param = param,
            navOption = navOption
        )
    }

    fun <T : Direction> startForResult(
        direction: T,
        launcher: ActivityResultLauncher<Intent>,
        navOption: NavOptions? = null
    ) {
        startInternal(
            destination = direction,
            launcher = launcher,
            navOption = navOption
        )
    }

    fun <P : DirectionParam, T : DirectionWithParam<P>> startForResult(
        direction: T,
        launcher: ActivityResultLauncher<Intent>,
        param: P,
        navOption: NavOptions? = null
    ) {
        startInternal(
            destination = direction,
            launcher = launcher,
            param = param,
            navOption = navOption
        )
    }

    private fun startInternal(
        destination: Destination,
        requestCode: Int? = null,
        param: DirectionParam? = null,
        navOption: NavOptions?
    ) {
        val routing = findRouting(destination) ?: return
        logger.d("matched route ${destination.path}")

        val intent = routing.createIntent(starter)
        applyBundle(param, intent, navOption)

        if (requestCode != null) {
            starter.startForResult(intent, requestCode)
        } else {
            starter.start(intent)
        }
    }

    private fun startInternal(
        destination: Destination,
        launcher: ActivityResultLauncher<Intent>,
        param: DirectionParam? = null,
        navOption: NavOptions? = null
    ) {
        val routing = findRouting(destination) ?: return
        logger.d("matched route ${destination.path}")

        val intent = routing.createIntent(starter)
        applyBundle(param, intent, navOption)

        launcher.launch(intent)
    }

    private fun findRouting(destination: Destination): CreateRouting? {
        val hasDestination = graph.containsDestination(destination)
        if (!hasDestination) {
            throw MissingRouteThrowable(routeName = destination.toString())
        }

        if (!starter.validStarter()) {
            logger.w("Current not valid to a starter")
            return null
        }

        return graph.getRequiredRouting(destination) as? CreateRouting
    }

    private fun applyBundle(
        param: DirectionParam?,
        intent: Intent,
        navOption: NavOptions?
    ) {
        if (param != null) {
            intent.putExtra(DIRECTION_PARAMS_KEY, param)
        }

        if (navOption != null) {
            if (navOption.singleTop) {
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
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
