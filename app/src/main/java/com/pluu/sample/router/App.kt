package com.pluu.sample.router

import android.app.Application
import com.pluu.navigator.DeepLinkConfig
import com.pluu.navigator.Navigator
import com.pluu.navigator.NavigatorController
import com.pluu.sample.feature1.navigator.Feature1Graph
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initNavigator()
    }

    // Sample
    private fun initNavigator() {
        val config = NavigatorController.Config(
            deepLinkConfig = DeepLinkConfig(
                path = "pluu"
            )
        )

        Navigator.registerConfig(config)

        Timber.d("Init Navigator")
        listOf(
            com.pluu.sample.featurehome.navigator.RouteProvider(),
            com.pluu.sample.feature1.navigator.RouteProvider(),
            com.pluu.sample.feature1.navigator.DeepLinkProvider(),
            com.pluu.sample.feature2.navigator.RouteProvider(),
            com.pluu.sample.feature2.navigator.RouteProviderForJava(),
            com.pluu.sample.feature2.navigator.DeepLinkProvider(),
            com.pluu.sample.feature2.navigator.DeepLinkProviderForJava(),
        ).forEach {
            it.provide()
        }

        Navigator.addDestinations(Feature1Graph)
    }
}