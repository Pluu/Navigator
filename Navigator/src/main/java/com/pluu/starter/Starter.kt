package com.pluu.starter

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

interface Starter {
    val context: Context?

    fun start(intent: Intent)

    fun startForResult(intent: Intent, requestCode: Int)
}

internal class ActivityStarter(private val activity: Activity) : Starter {
    override val context = activity

    override fun start(intent: Intent) {
        activity.startActivity(intent)
    }

    override fun startForResult(
        intent: Intent,
        requestCode: Int
    ) {
        activity.startActivityForResult(intent, requestCode)
    }
}

internal class FragmentStarter(private val fragment: Fragment) : Starter {
    override val context = fragment.context

    override fun start(intent: Intent) {
        fragment.startActivity(intent)
    }

    override fun startForResult(
        intent: Intent,
        requestCode: Int
    ) {
        fragment.startActivityForResult(intent, requestCode)
    }
}