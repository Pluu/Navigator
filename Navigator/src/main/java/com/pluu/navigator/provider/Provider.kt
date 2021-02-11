package com.pluu.navigator.provider

import com.pluu.navigator.*
import com.pluu.navigator.util.deepLinkProvider
import com.pluu.navigator.util.toRouting

interface Provider {
    fun provide()
}

fun navigatorProvider(
    action: () -> Unit
): Provider = pendingProvider {
    action()
}

fun routeProvider(
    targetRoute: AbstractRoute,
    creator: INTENT_CREATOR
): Provider = pendingProvider {
    targetRoute.register(creator)
}

fun deepLinkProvider(
    deepLink: String,
    executor: LINK_EXECUTOR
): Provider = pendingProvider {
    DeepLink(deepLink).register(executor.toRouting())
}

inline fun <reified T : DeepLinkCommand> deepLinkProvider(
    deepLink: String
): Provider = deepLink.deepLinkProvider<T>()

@PublishedApi
internal inline fun pendingProvider(
    crossinline action: () -> Unit
): Provider = object : Provider {
    override fun provide() {
        action()
    }
}