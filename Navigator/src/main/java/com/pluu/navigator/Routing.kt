package com.pluu.navigator

import android.content.Intent
import com.pluu.navigator.deeplink.DeepLinkMatchResult
import com.pluu.navigator.starter.Starter

typealias CREATOR_ACTION = (starter: Starter) -> Intent

typealias EXECUTOR_ACTION = (starter: Starter, result: DeepLinkMatchResult) -> Unit

interface Routing

///////////////////////////////////////////////////////////////////////////
// Route Creator
///////////////////////////////////////////////////////////////////////////

interface CreateRouting : Routing {
    fun createIntent(starter: Starter): Intent
}

internal class CreateRoutingImpl(
    private val creator: CREATOR_ACTION
) : CreateRouting {
    override fun createIntent(starter: Starter) = creator(starter)
}

///////////////////////////////////////////////////////////////////////////
// DeepLink Executor
///////////////////////////////////////////////////////////////////////////

interface AbstractExecutor : Routing

interface ExecuteRouting : AbstractExecutor {
    fun execute(starter: Starter, matched: DeepLinkMatchResult)
}

class CommandRouting<T : DeepLinkCommand>(
    val command: Class<T>
) : AbstractExecutor

interface DeepLinkCommand {
    fun execute(starter: Starter)
}
