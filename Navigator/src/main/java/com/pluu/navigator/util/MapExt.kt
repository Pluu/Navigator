package com.pluu.navigator.util

fun Map<String, Any?>.toArray() = map { (key, value) ->
    key to value
}.toTypedArray()
