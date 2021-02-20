package com.pluu.sample.routeconst

import com.pluu.navigator.Direction
import com.pluu.navigator.DirectionParam
import com.pluu.navigator.DirectionWithParam

object Home {
    object Default : Direction()
}

object Direction1 {
    object Feature1 : Direction()
    object Feature1Graph : Direction()
    object Feature1Graph2 : Direction()
}

object Direction2 {
    object Feature2 : DirectionWithParam<SampleParam>()
    object Feature2ForJava : DirectionWithParam<SampleParam>()
}

class SampleParam(
    val value: Int,
    val value2: String? = null
) : DirectionParam() {
    override fun toString(): String {
        return "SampleParam(value=$value, value2=$value2)"
    }
}
