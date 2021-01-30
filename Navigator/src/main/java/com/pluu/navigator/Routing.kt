package com.pluu.navigator

import android.content.Intent
import com.pluu.starter.Starter

typealias INTENT_CREATOR = (Starter) -> Intent

interface Routing {
    fun create(starter: Starter): Intent
}

class RoutingImpl(private val creator: INTENT_CREATOR) : Routing {
    override fun create(starter: Starter) = creator(starter)
}
