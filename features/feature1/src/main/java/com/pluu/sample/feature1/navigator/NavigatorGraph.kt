package com.pluu.sample.feature1.navigator

import android.content.Intent
import com.pluu.navigator.DeepLinkConfig
import com.pluu.navigator.routeGraph
import com.pluu.sample.feature1.Feature1Activity
import com.pluu.sample.routeconst.Routes1
import com.pluu.utils.buildIntent

val Feature1Graph = routeGraph(
    deepLinkConfig = DeepLinkConfig("feature1")
) {
    addRoute(Routes1.Feature1_Graph) { starter ->
        Intent(starter.context!!, Feature1Activity::class.java)
    }

    addDeepLink("1") { starter, _ ->
        val context = starter.context ?: return@addDeepLink
        starter.start(context.buildIntent<Feature1Activity>())
    }
}