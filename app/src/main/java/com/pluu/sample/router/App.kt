package com.pluu.sample.router

import android.app.Application
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initTimber()
        initNavigator()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    // Sample
    private fun initNavigator() {
        listOf(
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