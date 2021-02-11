package com.pluu.sample.feature1.navigator

import com.pluu.navigator.DeepLinkCommand
import com.pluu.navigator.provider.deepLinkProvider
import com.pluu.navigator.starter.Starter
import com.pluu.navigator.util.toArray
import com.pluu.sample.feature1.Feature1Activity
import com.pluu.utils.buildIntent

///////////////////////////////////////////////////////////////////////////
// DeepLink Functional Pattern Sample
///////////////////////////////////////////////////////////////////////////

// Provider
private val DeepLink_Simple =
    deepLinkProvider("pluu://feature1") { starter, _ ->
        starter.start(starter.context!!.buildIntent<Feature1Activity>())
    }

// Provider (Relative Path)
private val DeepLink_Relative_Path =
    deepLinkProvider("feature1/sample1?type={type}") { starter, deepLinkMatch ->
        val args = deepLinkMatch.args.toArray()
        starter.start(starter.context!!.buildIntent<Feature1Activity>(*args))
    }

// Provider (Command)
private val DeepLink_Command =
    deepLinkProvider<SampleCommand>("pluu://feature1/sample2?type={value}")

private class SampleCommand(
    private val value: Int
) : DeepLinkCommand {
    override fun execute(starter: Starter) {
        starter.start(starter.context!!.buildIntent<Feature1Activity>("value" to value))
    }
}

///////////////////////////////////////////////////////////////////////////
// Sample Definition
///////////////////////////////////////////////////////////////////////////

val sample_feature1_function_pattern = listOf(
    DeepLink_Simple,
    DeepLink_Relative_Path,
    DeepLink_Command
)