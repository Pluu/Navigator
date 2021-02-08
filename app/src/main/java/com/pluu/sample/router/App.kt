package com.pluu.sample.router

import android.app.Application
import com.pluu.navigator.Navigator
import com.pluu.navigator.NavigatorController
import com.pluu.sample.feature1.navigator.Feature1Graph
import com.pluu.sample.feature1.navigator.sample_feature1_function_pattern
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initNavigator()
    }

    // Sample
    private fun initNavigator() {
        val config = NavigatorController.Config(
            baseScheme = "pluu"
        )

        Navigator.registerConfig(config)

        Timber.d("Init Navigator")

        ///////////////////////////////////////////////////////////////////////////
        // Register provider
        ///////////////////////////////////////////////////////////////////////////
        listOf(
            // Function Pattern
            *sample_feature1_function_pattern.toTypedArray(),
            // Simple Provider pattern (Function)
            com.pluu.sample.featurehome.navigator.Home_Route_Provider,
            // Provider pattern (Interface)
            com.pluu.sample.feature1.navigator.RouteProvider(),
            // Simple Provider pattern
            com.pluu.sample.feature2.navigator.Feature1_Route_1,
            // Provider pattern (Interface)
            com.pluu.sample.feature2.navigator.DeepLinkProvider(),
            // Java Sample pattern
            com.pluu.sample.feature2.navigator.RouteProviderForJava(),
            com.pluu.sample.feature2.navigator.DeepLinkProviderForJava(),
        ).forEach {
            it.provide()
        }

        ///////////////////////////////////////////////////////////////////////////
        // Register Graph
        ///////////////////////////////////////////////////////////////////////////
        // Graph provider pattern
        Navigator.addDestinations(Feature1Graph)
        // or Feature1Graph.register()
    }
}