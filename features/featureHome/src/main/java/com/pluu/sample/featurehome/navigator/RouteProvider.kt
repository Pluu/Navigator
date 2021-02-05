package com.pluu.sample.featurehome.navigator

import android.content.Intent
import com.pluu.navigator.provider.Provider
import com.pluu.sample.featurehome.HomeActivity
import com.pluu.sample.routeconst.Home

class RouteProvider : Provider {
    override fun provide() {
        Home.Default.register { starter ->
            Intent(starter.context!!, HomeActivity::class.java)
        }
    }
}