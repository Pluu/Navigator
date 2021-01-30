package com.pluu.sample.feature2.navigator

import android.content.Intent
import com.pluu.navigator.provider.Provider
import com.pluu.sample.feature2.Feature2Activity
import com.pluu.sample.routeconst.Routes2

class RouteProvider : Provider() {
    override fun provide() {
        Routes2.Feature2.register { context ->
            Intent(context, Feature2Activity::class.java)
        }
    }
}