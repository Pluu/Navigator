package com.pluu.sample.featurehome.navigator

import android.content.Intent
import com.pluu.navigator.provider.pendingProvider
import com.pluu.sample.featurehome.HomeActivity
import com.pluu.sample.routeconst.Home

val Home_Route_Provider = pendingProvider {
    Home.Default.register { starter ->
        Intent(starter.context!!, HomeActivity::class.java)
    }
}
