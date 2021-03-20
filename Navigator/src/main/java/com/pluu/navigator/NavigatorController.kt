package com.pluu.navigator

import com.pluu.navigator.util.logger.Logger
import com.pluu.navigator.util.logger.NavigatorLogger

object NavigatorController {
    internal var config: Config = Config()

    fun setConfig(
        config: Config = Config()
    ) {
        this.config = config
        if (config.logger is NavigatorLogger) {
            config.logger.setup(BuildConfig.DEBUG)
        }
    }

    class Config(
        val baseScheme: String? = null,
        val logger: Logger = NavigatorLogger()
    )
}

internal inline val logger: Logger
    get() = NavigatorController.config.logger
