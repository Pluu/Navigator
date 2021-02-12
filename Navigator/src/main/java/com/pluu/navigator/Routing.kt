package com.pluu.navigator

import android.content.Intent
import com.pluu.navigator.deeplink.DeepLinkMatchResult
import com.pluu.navigator.starter.Starter

typealias INTENT_CREATOR = (Starter) -> Intent
typealias LINK_EXECUTOR = (Starter, DeepLinkMatchResult) -> Unit

///////////////////////////////////////////////////////////////////////////
// Route Creator
///////////////////////////////////////////////////////////////////////////

interface Routing

interface CreateRouting : Routing {
    fun create(starter: Starter): Intent
}

internal class CreateRoutingImpl(
    private val creator: INTENT_CREATOR
) : CreateRouting {
    override fun create(starter: Starter) = creator(starter)
}

///////////////////////////////////////////////////////////////////////////
// DeepLink Executor
///////////////////////////////////////////////////////////////////////////

interface AbstractExecutor : Routing

interface ExecuteRouting : AbstractExecutor {
    fun execute(starter: Starter, matched: DeepLinkMatchResult)
}

class CommandRouting<T : DeepLinkCommand>(val command: Class<T>) : AbstractExecutor

interface DeepLinkCommand {
    fun execute(starter: Starter)
}
