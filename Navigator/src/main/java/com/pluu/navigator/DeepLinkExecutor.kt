package com.pluu.navigator

import com.google.gson.Gson
import com.pluu.navigator.deeplink.DeepLinkMatchResult
import com.pluu.navigator.starter.Starter
import org.json.JSONObject

class DeepLinkExecutor {
    private val gson by lazy { Gson() }

    fun execute(
        executor: AbstractExecutor,
        starter: Starter,
        deepLinkMatchResult: DeepLinkMatchResult
    ): Boolean = when (executor) {
        is ExecuteRouting -> {
            executor.execute(starter, deepLinkMatchResult)
            true
        }
        is CommandRouting<*> -> {
            val json = JSONObject()
            for ((key, value) in deepLinkMatchResult.args) {
                json.put(key, value)
            }
            val command = gson.fromJson(json.toString(), executor.command)
            command.execute(starter)
            true
        }
        else -> false
    }
}