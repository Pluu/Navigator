package com.pluu.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.os.bundleOf

fun Context.buildIntent(
    target: Class<Activity>,
    vararg pairs: Pair<String, Any?>
) = Intent(this, target).apply {
    putExtras(bundleOf(*pairs))
}

inline fun <reified T : Activity> Context.buildIntent(
    vararg argument: Pair<String, Any?>
) = Intent(this, T::class.java).apply {
    putExtras(bundleOf(*argument))
}

inline fun <reified T : Activity> Context.startActivity(
    vararg argument: Pair<String, Any?>
) {
    startActivity(buildIntent<T>(*argument))
}
