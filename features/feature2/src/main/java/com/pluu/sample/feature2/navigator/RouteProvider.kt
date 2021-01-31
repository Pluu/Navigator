package com.pluu.sample.feature2.navigator

import com.pluu.navigator.provider.Provider
import com.pluu.sample.feature2.Feature2Activity
import com.pluu.sample.routeconst.Routes2
import com.pluu.utils.buildIntent

class RouteProvider : Provider {
    override fun provide() {
        Routes2.Feature2.register { starter ->
            starter.context!!.buildIntent<Feature2Activity>()
        }
    }
}
