package com.pluu.sample.feature1.navigator

import com.pluu.navigator.provider.deepLinkProvider
import com.pluu.sample.feature1.Feature1Activity
import com.pluu.utils.buildIntent

val Feature1_DeepLink_1 = deepLinkProvider("pluu://feature1") { starter, deepLinkMatch ->
    val context = starter.context ?: return@deepLinkProvider
    starter.start(context.buildIntent<Feature1Activity>())
}

val Feature1_DeepLink_2 = deepLinkProvider("feature1/sample1?type={type}") { starter, deepLinkMatch ->
    val context = starter.context ?: return@deepLinkProvider

    val args = deepLinkMatch.args.map { (key, value) ->
        key to value
    }.toTypedArray()

    starter.start(context.buildIntent<Feature1Activity>(*args))
}
