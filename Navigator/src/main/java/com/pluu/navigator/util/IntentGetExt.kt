package com.pluu.navigator.util

import android.app.Activity
import com.pluu.navigator.ROUTE_PARAMS_KEY
import kotlin.properties.ReadOnlyProperty

fun <T> bindExtra() = extraDelegate {
    it.intent.extras!!.get(ROUTE_PARAMS_KEY) as T
}

fun <T> optionalBindExtra() = extraDelegate {
    it.intent.extras?.get(ROUTE_PARAMS_KEY) as? T
}

fun <T, R> bindExtra(
    getter: (param: T) -> R
) = extraDelegate {
    val args: T = it.intent.extras!!.get(ROUTE_PARAMS_KEY) as T
    getter(args)
}

fun <T, R> optionalBindExtra(
    getter: (param: T?) -> R?
) = extraDelegate {
    val args = it.intent.extras?.get(ROUTE_PARAMS_KEY) as? T
    getter(args)
}

private fun <R> extraDelegate(
    getter: (Activity) -> R
) = ReadOnlyProperty<Activity, R> { thisRef, _ ->
    getter.invoke(thisRef)
}