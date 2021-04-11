package com.pluu.navigator.util

import com.pluu.navigator.AbstractExecutor
import com.pluu.navigator.CREATOR_ACTION
import com.pluu.navigator.CommandRouting
import com.pluu.navigator.CreateRoutingImpl
import com.pluu.navigator.DeepLink
import com.pluu.navigator.DeepLinkCommand
import com.pluu.navigator.EXECUTOR_ACTION
import com.pluu.navigator.ExecuteRouting
import com.pluu.navigator.RouteGraph
import com.pluu.navigator.Routing
import com.pluu.navigator.deeplink.DeepLinkMatchResult
import com.pluu.navigator.provider.Provider
import com.pluu.navigator.provider.pendingProvider
import com.pluu.navigator.starter.Starter

internal fun CREATOR_ACTION.toRouting(): Routing = CreateRoutingImpl(this)

internal fun EXECUTOR_ACTION.toRouting(): AbstractExecutor = object : ExecuteRouting {
    override fun execute(starter: Starter, matched: DeepLinkMatchResult) {
        invoke(starter, matched)
    }
}

internal fun <T : DeepLinkCommand> Class<T>.toRouting(): CommandRouting<T> {
    return CommandRouting(this)
}

@PublishedApi
internal inline fun <reified T : DeepLinkCommand> String.deepLinkProvider(): Provider = pendingProvider {
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
