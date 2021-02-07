package com.pluu.navigator

import com.pluu.navigator.util.enclosingClassFullSimpleName

open class Destination {
    var path: String = javaClass.enclosingClassFullSimpleName()
        private set

    fun setPath(path: String) {
        this.path = path
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Destination

        if (path != other.path) return false

        return true
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }

    override fun toString(): String {
        return "Destination(path='$path')"
    }
}