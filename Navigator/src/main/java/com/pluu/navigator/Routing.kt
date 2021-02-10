package com.pluu.navigator

import android.content.Intent
import com.pluu.navigator.deeplink.DeepLinkMatch
import com.pluu.navigator.starter.Starter

typealias INTENT_CREATOR = (Starter) -> Intent
typealias LINK_EXECUTOR = (Starter, DeepLinkMatch) -> Unit

///////////////////////////////////////////////////////////////////////////
// Route
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
// DeepLink
///////////////////////////////////////////////////////////////////////////

interface AbstractExecutor : Routing

interface Command {
    fun execute(starter: Starter)
}

class CommandRouting<T : Command>(val command: Class<T>) : AbstractExecutor

interface ExecuteRouting : AbstractExecutor {
    fun execute(starter: Starter, matched: DeepLinkMatch)
}
