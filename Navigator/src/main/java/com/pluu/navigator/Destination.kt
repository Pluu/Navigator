package com.pluu.navigator

import com.pluu.navigator.util.enclosingClassFullSimpleName

open class Destination {
    var path: String = javaClass.enclosingClassFullSimpleName()
        private set

    fun setPath(path: String) {
        this.path = path
    }
}