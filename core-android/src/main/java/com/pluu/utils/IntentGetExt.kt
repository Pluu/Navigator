package com.pluu.utils

import android.app.Activity
import kotlin.reflect.KProperty

class ExtraDelegate<T>(
    private val key: String
) {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        if (thisRef != null && thisRef is Activity) {
            return thisRef.getExtra(key)
        }
        throw Throwable("required parameter d")
    }
}

@Suppress("NOTHING_TO_INLINE", "UNCHECKED_CAST")
private inline fun <T> Activity.getExtra(
    key: String
): T = intent.extras!!.get(key) as T