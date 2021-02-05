package com.pluu.sample.feature1.navigator

import android.content.Intent
import com.pluu.navigator.provider.Provider
import com.pluu.sample.feature1.Feature1Activity
import com.pluu.sample.routeconst.Routes1

class RouteProvider : Provider {
    override fun provide() {
        Routes1.Feature1.register { starter ->
            Intent(starter.context!!, Feature1Activity::class.java)
        }
    }
}