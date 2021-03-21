package com.pluu.navigator.provider

import com.pluu.navigator.AbstractRoute
import com.pluu.navigator.CREATOR_ACTION
import com.pluu.navigator.DeepLink
import com.pluu.navigator.DeepLinkCommand
import com.pluu.navigator.EXECUTOR_ACTION
import com.pluu.navigator.util.deepLinkProvider
import com.pluu.navigator.util.toRouting

interface Provider {
    fun provide()
}

inline fun pendingProvider(
    crossinline action: () -> Unit
): Provider = object : Provider {
    override fun provide() {
        action()
    }
}

fun routeProvider(
    targetRoute: AbstractRoute,
    creator: CREATOR_ACTION
): Provider = pendingProvider {
    targetRoute.register(creator)
}

fun deepLinkProvider(
    deepLink: String,
    executor: EXECUTOR_ACTION
): Provider = pendingProvider {
    DeepLink(deepLink).register(executor.toRouting())
}

inline fun <reified T : DeepLinkCommand> deepLinkProvider(
    deepLink: String
): Provider = deepLink.deepLinkProvider<T>()
