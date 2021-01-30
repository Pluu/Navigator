package com.pluu.sample.router

import android.app.Application
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        initTimber()
        initNavigator()
        initDeepLink()
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
            com.pluu.sample.feature2.navigator.RouteProvider(),
        ).forEach {
            it.provide()
        }
    }

    // Sample
    private fun initDeepLink() {
        listOf(
            com.pluu.sample.feature1.deeplink.DeepLinkProvider(),
            com.pluu.sample.feature2.deeplink.DeepLinkProvider(),
        ).forEach {
            it.provide()
        }
    }
}