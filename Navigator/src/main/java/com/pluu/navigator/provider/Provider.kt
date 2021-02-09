package com.pluu.navigator.provider

import com.pluu.navigator.AbstractRoute
import com.pluu.navigator.Command
import com.pluu.navigator.INTENT_CREATOR
import com.pluu.navigator.LINK_EXECUTOR
import com.pluu.navigator.deeplink.DeepLink
import com.pluu.navigator.util.toDeepLinkProvider
import com.pluu.navigator.util.toRouting

interface Provider {
    fun provide()
}

fun navigatorProvider(
    action: () -> Unit
): Provider = object : Provider {
    override fun provide() {
        action()
    }
}

fun routeProvider(
    targetRoute: AbstractRoute,
    creator: INTENT_CREATOR
): Provider = object : Provider {
    override fun provide() {
        targetRoute.register(creator)
    }
}

fun deepLinkProvider(
    deepLink: String,
    executor: LINK_EXECUTOR
): Provider = object : Provider {
    override fun provide() {
        DeepLink(deepLink).register(executor.toRouting())
    }
}

inline fun <reified T : Command> deepLinkProvider(
    deepLink: String
): Provider = deepLink.toDeepLinkProvider<T>()
