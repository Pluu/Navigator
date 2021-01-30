package com.pluu.navigator

import android.content.Context
import android.content.Intent

typealias INTENT_CREATOR = (Context) -> Intent

interface Routing {
    fun create(context: Context): Intent
}

class RoutingImpl(private val creator: INTENT_CREATOR) : Routing {
    override fun create(context: Context) = creator(context)
}
