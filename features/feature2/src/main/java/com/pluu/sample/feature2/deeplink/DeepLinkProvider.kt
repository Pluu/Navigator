package com.pluu.sample.feature2.deeplink

import com.pluu.deeplink.DeepLink
import com.pluu.provider.Provider
import com.pluu.sample.feature2.Feature2Activity
import com.pluu.utils.buildIntent

class DeepLinkProvider : Provider {
    override fun provide() {
        DeepLink("pluudeep://feature2").register { starter, _ ->
            val context = starter.context ?: return@register
            starter.start(context.buildIntent<Feature2Activity>())
        }
    }
}