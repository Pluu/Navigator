package com.pluu.navigator

import android.content.Intent
import com.pluu.navigator.deeplink.DeepLinkMatch
import com.pluu.starter.Starter

typealias INTENT_CREATOR = (Starter) -> Intent
typealias LINK_EXECUTOR = (Starter, DeepLinkMatch) -> Unit

fun LINK_EXECUTOR.toRouting(): AbstractExecutor = object : ExecuteRouting {
    override fun execute(starter: Starter, matched: DeepLinkMatch) {
        invoke(starter, matched)
    }
}

inline fun <reified T : Command> String.toDeepLinkRouting(): AbstractExecutor =
    CommandRouting(T::class.java)

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
