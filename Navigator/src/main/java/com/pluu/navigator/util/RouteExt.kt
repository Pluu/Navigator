package com.pluu.navigator.util

import com.pluu.navigator.*
import com.pluu.navigator.deeplink.DeepLink
import com.pluu.navigator.deeplink.DeepLinkMatch
import com.pluu.navigator.provider.Provider
import com.pluu.starter.Starter

fun LINK_EXECUTOR.toRouting(): AbstractExecutor = object : ExecuteRouting {
    override fun execute(starter: Starter, matched: DeepLinkMatch) {
        invoke(starter, matched)
    }
}

inline fun <reified T : Command> String.toDeepLinkProvider(): Provider = object : Provider {
    override fun provide() {
        DeepLink(this@toDeepLinkProvider).register(CommandRouting(T::class.java))
    }
}
