package com.pluu.utils

import android.app.Activity
import android.widget.Toast

fun Activity.showToast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}