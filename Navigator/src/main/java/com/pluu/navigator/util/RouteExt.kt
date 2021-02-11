package com.pluu.navigator.util

import com.pluu.navigator.*
import com.pluu.navigator.deeplink.DeepLinkMatch
import com.pluu.navigator.provider.Provider
import com.pluu.navigator.provider.pendingProvider
import com.pluu.navigator.starter.Starter

fun LINK_EXECUTOR.toRouting(): AbstractExecutor = object : ExecuteRouting {
    override fun execute(starter: Starter, matched: DeepLinkMatch) {
        invoke(starter, matched)
    }
}

inline fun <reified T : DeepLinkCommand> String.deepLinkProvider(): Provider = pendingProvider {
    DeepLink(this).register(T::class.java)
}

inline fun <reified T : DeepLinkCommand> DeepLink.register() {
    register(T::class.java)
}

inline fun <reified T : DeepLinkCommand> RouteGraph.Builder.addDeepLink(
    path: String
) {
    addDeepLink(path, T::class.java)
}
