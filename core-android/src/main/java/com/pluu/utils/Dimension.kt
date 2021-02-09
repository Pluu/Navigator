package com.pluu.utils

import android.content.Context

fun Context.dp2px(dp: Float): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

