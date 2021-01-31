package com.pluu.sample.feature1.navigator

import com.pluu.navigator.deeplink.DeepLink
import com.pluu.navigator.provider.Provider
import com.pluu.sample.feature1.Feature1Activity
import com.pluu.utils.buildIntent

class DeepLinkProvider : Provider {
    override fun provide() {
        DeepLink("pluu://feature1").register { starter, _ ->
            val context = starter.context ?: return@register
            starter.start(context.buildIntent<Feature1Activity>())
        }
    }
}