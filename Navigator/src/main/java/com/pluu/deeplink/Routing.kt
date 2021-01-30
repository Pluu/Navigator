package com.pluu.deeplink

import com.pluu.starter.Starter

typealias LINK_EXECUTOR = (Starter, DeepLinkMatch) -> Unit

interface Routing {
    fun execute(starter: Starter, matched: DeepLinkMatch)
}

class RoutingImpl(
    private val executor: LINK_EXECUTOR
) : Routing {
    override fun execute(
        starter: Starter,
        matched: DeepLinkMatch
    ) = executor(starter, matched)
}