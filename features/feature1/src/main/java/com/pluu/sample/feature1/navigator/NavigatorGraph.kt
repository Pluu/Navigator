package com.pluu.sample.feature1.navigator

import android.content.Intent
import com.pluu.navigator.*
import com.pluu.navigator.starter.Starter
import com.pluu.navigator.util.toArray
import com.pluu.sample.feature1.Feature1Activity
import com.pluu.sample.feature1.Feature1SubActivity
import com.pluu.sample.routeconst.Routes1
import com.pluu.utils.buildIntent

///////////////////////////////////////////////////////////////////////////
// Route Graph Sample
///////////////////////////////////////////////////////////////////////////

// Functional pattern
private val Feature1_Graph = routeGraph(
    graphName = "feature1",
    deepLinkConfig = DeepLinkConfig("feature1_graph")
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

    addDeepLink("luckystar://izumi/konata") { starter, _ ->
        starter.start(starter.context!!.buildIntent<Feature1SubActivity>())
    }

    addDeepLink<SampleFeature1Command_1>("sample2?type={value}")
}

private class SampleFeature1Command_1(
    private val value: Int
) : Command {
    override fun execute(starter: Starter) {
        starter.start(starter.context!!.buildIntent<Feature1Activity>("Command_1" to value))
    }
}

// Builder pattern
private val Feature1_GraphBuilder = RouteGraph.Builder(
    graphName = "feature1_1",
    deepLinkConfig = DeepLinkConfig("feature1_1")
).apply {
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

    addDeepLink("luckystar://izumi/konata") { starter, _ ->
        starter.start(starter.context!!.buildIntent<Feature1SubActivity>())
    }

    addDeepLink<SampleFeature1Command_2>("pluu://feature_1_1/sample2?type={value}")
}

private class SampleFeature1Command_2(
    private val value: Int
) : Command {
    override fun execute(starter: Starter) {
        starter.start(starter.context!!.buildIntent<Feature1Activity>("Command_2" to value))
    }
}

///////////////////////////////////////////////////////////////////////////
// Sample Definition
///////////////////////////////////////////////////////////////////////////

val sample_feature1_graph_function_pattern = listOf(
    Feature1_Graph,
    Feature1_GraphBuilder.build()
)
