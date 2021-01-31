package com.pluu.navigator.util

fun Class<*>.enclosingClassFullSimpleName(): String {
    val enclosingClass = enclosingClass
    return if (enclosingClass != null) {
        name.substring(enclosingClass.name.lastIndexOf(".") + 1)
    } else {
        simpleName
    }
}