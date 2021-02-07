package com.pluu.sample.feature1.navigator

import com.pluu.navigator.deeplink.DeepLink
import com.pluu.navigator.deeplink.DeepLinkMatch
import com.pluu.navigator.provider.Provider
import com.pluu.sample.feature1.Feature1Activity
import com.pluu.starter.Starter
import com.pluu.utils.buildIntent
import timber.log.Timber

class DeepLinkProvider : Provider {
    override fun provide() {
        DeepLink("pluu://feature1").register { starter, deepLinkMatch ->
            start(starter, deepLinkMatch)
        }

        DeepLink("feature1/sample1?type={type}").register { starter, deepLinkMatch ->
            start(starter, deepLinkMatch)
        }
    }

    private fun start(starter: Starter, deepLinkMatch: DeepLinkMatch) {
        val context = starter.context ?: return

        Timber.tag("Feature1").d("Destination >> ${deepLinkMatch.destination.path}")
        Timber.tag("Feature1").d("Request >> ${deepLinkMatch.request}")

        val args = deepLinkMatch.args.map { (key, value) ->
            key to value
        }.toTypedArray()

        starter.start(context.buildIntent<Feature1Activity>(*args))
    }
}