package com.pluu.navigator.util

import android.app.Activity
import android.os.Bundle
import com.pluu.navigator.DIRECTION_PARAMS_KEY
import kotlin.properties.ReadOnlyProperty

fun <T> Bundle.findDirectionParam() = get(DIRECTION_PARAMS_KEY) as T

fun <T> Bundle.optionalDirectionParam() = get(DIRECTION_PARAMS_KEY) as? T

fun <T> bindExtra() = extraDelegate {
    it.intent.extras!!.findDirectionParam<T>()
}

fun <T> optionalBindExtra() = extraDelegate {
    it.intent.extras?.optionalDirectionParam<T>()
}

fun <T, R> bindExtra(
    getter: (param: T) -> R
) = extraDelegate {
    val args: T = it.intent.extras!!.findDirectionParam()
    getter(args)
}

fun <T, R> optionalBindExtra(
    getter: (param: T?) -> R?
) = extraDelegate {
    val args = it.intent.extras?.optionalDirectionParam<T>()
    getter(args)
}

private fun <R> extraDelegate(
    getter: (Activity) -> R
) = ReadOnlyProperty<Activity, R> { thisRef, _ ->
    getter.invoke(thisRef)
}