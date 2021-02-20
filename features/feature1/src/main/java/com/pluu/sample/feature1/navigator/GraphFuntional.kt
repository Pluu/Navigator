package com.pluu.sample.feature1.navigator

import android.content.Intent
import com.pluu.navigator.DeepLinkCommand
import com.pluu.navigator.DeepLinkConfig
import com.pluu.navigator.RouteGraph
import com.pluu.navigator.routeGraph
import com.pluu.navigator.starter.Starter
import com.pluu.navigator.util.addDeepLink
import com.pluu.navigator.util.toArray
import com.pluu.sample.feature1.Feature1Activity
import com.pluu.sample.feature1.Feature1SubActivity
import com.pluu.sample.routeconst.Direction1
import com.pluu.utils.buildIntent

///////////////////////////////////////////////////////////////////////////
// Route Graph Sample
///////////////////////////////////////////////////////////////////////////

// Functional pattern
private val Feature1_Graph = routeGraph(
    graphName = "feature1",
    deepLinkConfig = DeepLinkConfig("feature1_graph")
) {
    addDestination(Direction1.Feature1Graph) { starter ->
        Intent(starter.context, Feature1Activity::class.java)
    }

    addDeepLink("/") { starter, _ ->
        starter.start(starter.context.buildIntent<Feature1Activity>())
    }

    addDeepLink("/sample1?type={type}") { starter, result->
        val args = result.args.toArray()
        starter.start(starter.context.buildIntent<Feature1Activity>(*args))
    }

    addDeepLink("sub") { starter, _ ->
        starter.start(starter.context.buildIntent<Feature1SubActivity>())
    }

    addDeepLink("luckystar://izumi/konata") { starter, _ ->
        starter.start(starter.context.buildIntent<Feature1SubActivity>())
    }

    addDeepLink<SampleFeature1Command_1>("sample2?type={value}")
}

private class SampleFeature1Command_1(
    private val value: Int
) : DeepLinkCommand {
    override fun execute(starter: Starter) {
        starter.start(starter.context.buildIntent<Feature1Activity>("Command_1" to value))
    }
}

// Builder pattern
private val Feature1_GraphBuilder = RouteGraph.Builder(
    graphName = "feature1_1",
    deepLinkConfig = DeepLinkConfig("feature1_1")
).apply {
    addDestination(Direction1.Feature1Graph2) { starter ->
        Intent(starter.context, Feature1Activity::class.java)
    }

    addDeepLink("/") { starter, _ ->
        starter.start(starter.context.buildIntent<Feature1Activity>())
    }

    addDeepLink("/sample1?type={type}") { starter, result->
        val args = result.args.toArray()
        starter.start(starter.context.buildIntent<Feature1Activity>(*args))
    }

    addDeepLink("sub") { starter, _ ->
        starter.start(starter.context.buildIntent<Feature1SubActivity>())
    }

    addDeepLink("luckystar://izumi/konata2") { starter, _ ->
        starter.start(starter.context.buildIntent<Feature1SubActivity>())
    }

    addDeepLink<SampleFeature1Command_2>("pluu://feature_1_1/sample2?type={value}")
}

private class SampleFeature1Command_2(
    private val value: Int
) : DeepLinkCommand {
    override fun execute(starter: Starter) {
        starter.start(starter.context.buildIntent<Feature1Activity>("Command_2" to value))
    }
}

///////////////////////////////////////////////////////////////////////////
// Sample Definition
///////////////////////////////////////////////////////////////////////////

val sample_feature1_graph_function_pattern = listOf(
    Feature1_Graph,
    Feature1_GraphBuilder.build()
)
