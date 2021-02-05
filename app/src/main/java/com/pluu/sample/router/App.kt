package com.pluu.sample.router

import android.app.Application
import com.pluu.navigator.NavigatorController
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initNavigator()
    }

    // Sample
    private fun initNavigator() {
        NavigatorController.setConfig()

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
    }
}