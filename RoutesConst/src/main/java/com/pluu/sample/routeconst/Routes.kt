package com.pluu.sample.routeconst

import com.pluu.navigator.Route
import com.pluu.navigator.RouteParam
import com.pluu.navigator.RouteWithParam

object Home {
    object Default : Route()
}

object Routes1 {
    object Feature1 : Route()
    object Feature1Graph : Route()
    object Feature1Graph2 : Route()
}

object Routes2 {
    object Feature2 : RouteWithParam<SampleParam>()
    object Feature2ForJava : RouteWithParam<SampleParam>()
}

class SampleParam(
    val value: Int
) : RouteParam()
