package com.pluu.sample.feature2.navigator

import com.pluu.navigator.provider.routeProvider
import com.pluu.sample.feature2.Feature2Activity
import com.pluu.sample.routeconst.Direction2
import com.pluu.utils.buildIntent

internal val Feature1_Route_1 = routeProvider(
    Direction2.Feature2
) { starter ->
    starter.context.buildIntent<Feature2Activity>()
}
