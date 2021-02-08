package com.pluu.navigator.provider

import com.pluu.navigator.*
import com.pluu.navigator.deeplink.DeepLink

interface Provider {
    fun provide()
}

fun navigatorProvider(
    action: () -> Unit
) = object : Provider {
    override fun provide() {
        action()
    }
}

fun routeProvider(
    targetRoute: AbstractRoute,
    creator: INTENT_CREATOR
) = object : Provider {
    override fun provide() {
        targetRoute.register(creator)
    }
}

inline fun <reified T : Command> deepLinkProvider(
    deepLink: String
) = object : Provider {
    override fun provide() {
        DeepLink(deepLink).register(deepLink.toDeepLinkRouting<T>())
    }
}

fun deepLinkProvider(
    deepLink: String,
    executor: LINK_EXECUTOR
) = object : Provider {
    override fun provide() {
        DeepLink(deepLink).register(executor.toRouting())
    }
}
