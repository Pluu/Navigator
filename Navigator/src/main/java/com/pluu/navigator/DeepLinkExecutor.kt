package com.pluu.navigator

import com.google.gson.Gson
import com.pluu.navigator.deeplink.DeepLinkMatch
import com.pluu.navigator.starter.Starter
import org.json.JSONObject

class DeepLinkExecutor {
    private val gson = Gson()

    fun execute(
        executor: AbstractExecutor,
        starter: Starter,
        deepLinkMatch: DeepLinkMatch
    ): Boolean = when (executor) {
        is ExecuteRouting -> {
            executor.execute(starter, deepLinkMatch)
            true
        }
        is CommandRouting<*> -> {
            val json = JSONObject()
            for ((key, value) in deepLinkMatch.args) {
                json.put(key, value)
            }
            val command = gson.fromJson(json.toString(), executor.command)
            command.execute(starter)
            true
        }
        else -> false
    }
}