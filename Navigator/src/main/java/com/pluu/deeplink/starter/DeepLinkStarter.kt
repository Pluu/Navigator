package com.pluu.deeplink.starter

import androidx.core.net.toUri
import com.pluu.deeplink.DeepLinkRequest
import com.pluu.deeplink.RoutingProvider
import com.pluu.starter.Starter

class DeepLinkStarter(
    private val starter: Starter,
    private val routingProvider: RoutingProvider
) {
    fun execute(path: String): Boolean {
        return execute(DeepLinkRequest(path.toUri()))
    }

    fun execute(request: DeepLinkRequest): Boolean {
        val deepLinkMatch = routingProvider.matchDeepLink(request) ?: return false
        val routing = routingProvider.getRequiredRouting(deepLinkMatch.destination) ?: return false
        routing.execute(starter, deepLinkMatch)
        return true
    }
}
