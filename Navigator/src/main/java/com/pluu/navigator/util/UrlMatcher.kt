package com.pluu.navigator.util

private val SCHEME_PATTERN = "^[a-zA-Z]+[+\\w\\-.]*:".toPattern()

fun String.hasScheme() = SCHEME_PATTERN.matcher(this).find()