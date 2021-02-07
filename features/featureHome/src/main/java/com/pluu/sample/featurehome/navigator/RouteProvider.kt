package com.pluu.sample.featurehome.navigator

import android.content.Intent
import com.pluu.navigator.provider.navigatorProvider
import com.pluu.sample.featurehome.HomeActivity
import com.pluu.sample.routeconst.Home

val Home_Route_Provider = navigatorProvider {
    Home.Default.register { starter ->
        Intent(starter.context!!, HomeActivity::class.java)
    }
}
