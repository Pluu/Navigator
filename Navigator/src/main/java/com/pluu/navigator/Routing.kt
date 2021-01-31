package com.pluu.navigator

import android.content.Intent
import com.pluu.navigator.deeplink.DeepLinkMatch
import com.pluu.starter.Starter

typealias INTENT_CREATOR = (Starter) -> Intent
typealias LINK_EXECUTOR = (Starter, DeepLinkMatch) -> Unit

interface Routing

interface CreateRouting : Routing {
    fun create(starter: Starter): Intent
}

internal class CreateRoutingImpl(
    private val creator: INTENT_CREATOR
) : CreateRouting {
    override fun create(starter: Starter) = creator(starter)
}

interface ExecuteRouting : Routing {
    fun execute(starter: Starter, matched: DeepLinkMatch)
}

internal class ExecuteRoutingImpl(
    private val executor: LINK_EXECUTOR
) : ExecuteRouting {
    override fun execute(
        starter: Starter,
        matched: DeepLinkMatch
    ) = executor(starter, matched)
}