package com.pluu.sample.feature2.navigator

import com.pluu.navigator.DIRECTION_PARAMS_KEY
import com.pluu.navigator.DeepLink
import com.pluu.navigator.DeepLinkCommand
import com.pluu.navigator.provider.Provider
import com.pluu.navigator.starter.Starter
import com.pluu.navigator.util.register
import com.pluu.sample.feature2.Feature2Activity
import com.pluu.sample.feature2.Feature2SubActivity
import com.pluu.sample.routeconst.SampleParam
import com.pluu.utils.buildIntent

internal class DeepLinkProvider : Provider {
    override fun provide() {
        DeepLink("pluu://feature2").register { starter, _ ->
            starter.start(
                starter.context.buildIntent<Feature2Activity>(
                    DIRECTION_PARAMS_KEY to SampleParam(100)
                )
            )
        }
    }
}

internal class DeepLinkCommandProvider : Provider {
    override fun provide() {
        DeepLink("pluu://feature2/command?type={type}").register(Feature2SampleCommand::class.java)
    }
}

internal class DeepLinkCommandExtensionProvider : Provider {
    override fun provide() {
        DeepLink("pluu://feature2/command/extension?type={type}").register<Feature2SampleCommand>()
    }
}

class Feature2SampleCommand(
    private val type: String
) : DeepLinkCommand {
    override fun execute(starter: Starter) {
        starter.start(starter.context.buildIntent<Feature2SubActivity>("type" to type))
    }
}
