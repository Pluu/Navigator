package com.pluu.sample.feature2.navigator

import com.pluu.navigator.provider.routeProvider
import com.pluu.sample.feature2.Feature2Activity
import com.pluu.sample.routeconst.Routes2
import com.pluu.utils.buildIntent

val Feature1_Route_1 = routeProvider(
    Routes2.Feature2
) { starter ->
    starter.context!!.buildIntent<Feature2Activity>()
}
