package com.pluu.sample.router

import android.app.Application
import com.pluu.navigator.Navigator
import com.pluu.navigator.NavigatorController
import com.pluu.sample.feature1.navigator.sample_feature1_function_pattern
import com.pluu.sample.feature1.navigator.sample_feature1_graph_function_pattern
import com.pluu.sample.feature1.navigator.sample_feature1_provider_pattern
import com.pluu.sample.feature2.navigator.sample_feature2_pattern
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
            // Simple Provider pattern (Function)
            com.pluu.sample.featurehome.navigator.Home_Route_Provider,
            // Function Pattern
            *sample_feature1_function_pattern.toTypedArray(),
            // Provider pattern (Interface)
            *sample_feature1_provider_pattern.toTypedArray(),
            // Feature 2 samples
            *sample_feature2_pattern.toTypedArray()
        ).forEach {
            it.provide()
        }

        ///////////////////////////////////////////////////////////////////////////
        // Register Graph
        ///////////////////////////////////////////////////////////////////////////
        listOf(
            *sample_feature1_graph_function_pattern.toTypedArray()
        ).forEach {
            Navigator.addDestinations(it)
        }
    }
}