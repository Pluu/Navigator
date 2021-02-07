package com.pluu.navigator.provider

import com.pluu.navigator.AbstractRoute
import com.pluu.navigator.INTENT_CREATOR
import com.pluu.navigator.LINK_EXECUTOR
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

fun deepLinkProvider(
    deepLink: String,
    executor: LINK_EXECUTOR
) = object : Provider {
    override fun provide() {
        DeepLink(deepLink).register(executor)
    }
}