package com.pluu.sample.feature2.navigator

import com.pluu.navigator.ROUTE_PARAMS_KEY
import com.pluu.navigator.deeplink.DeepLink
import com.pluu.navigator.provider.Provider
import com.pluu.sample.feature2.Feature2Activity
import com.pluu.sample.routeconst.SampleParam
import com.pluu.utils.buildIntent

class DeepLinkProvider : Provider {
    override fun provide() {
        DeepLink("pluu://feature2").register { starter, _ ->
            val context = starter.context ?: return@register
            starter.start(context.buildIntent<Feature2Activity>(
                ROUTE_PARAMS_KEY to SampleParam(100)
            ))
        }
    }
}