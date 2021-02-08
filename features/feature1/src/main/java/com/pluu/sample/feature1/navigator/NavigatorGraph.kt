package com.pluu.sample.feature1.navigator

import android.content.Intent
import com.pluu.navigator.DeepLinkConfig
import com.pluu.navigator.RouteGraph
import com.pluu.navigator.routeGraph
import com.pluu.navigator.util.toArray
import com.pluu.sample.feature1.Feature1Activity
import com.pluu.sample.feature1.Feature1SubActivity
import com.pluu.sample.routeconst.Routes1
import com.pluu.utils.buildIntent

///////////////////////////////////////////////////////////////////////////
// Route Graph Sample
///////////////////////////////////////////////////////////////////////////

// Functional pattern
val Feature1_Graph = routeGraph(
    graphName = "feature1",
    deepLinkConfig = DeepLinkConfig("feature1")
) {
    addRoute(Routes1.Feature1_Graph) { starter ->
        Intent(starter.context!!, Feature1Activity::class.java)
    }

    addDeepLink("/") { starter, _ ->
        starter.start(starter.context!!.buildIntent<Feature1Activity>())
    }

    addDeepLink("/sample1?type={type}") { starter, deepLinkMatch ->
        val args = deepLinkMatch.args.toArray()
        starter.start(starter.context!!.buildIntent<Feature1Activity>(*args))
    }

    addDeepLink("sub") { starter, _ ->
        starter.start(starter.context!!.buildIntent<Feature1SubActivity>())
    }

    // TODO : Command sample

    addDeepLink("luckystar://izumi/konata") { starter, _ ->
        starter.start(starter.context!!.buildIntent<Feature1SubActivity>())
    }
}

// Builder pattern
val Feature1_GraphBuilder = RouteGraph.Builder(
    graphName = "feature1",
    deepLinkConfig = DeepLinkConfig("feature1")
).apply {
    // TODO
}
