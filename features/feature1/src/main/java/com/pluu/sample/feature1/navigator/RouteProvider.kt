package com.pluu.sample.feature1.navigator

import com.pluu.navigator.provider.Provider
import com.pluu.sample.feature1.Feature1Activity
import com.pluu.sample.routeconst.Routes1
import com.pluu.utils.buildIntent

class RouteProvider : Provider {
    override fun provide() {
        Routes1.Feature1.register { starter ->
            starter.context!!.buildIntent<Feature1Activity>()
        }
    }
}