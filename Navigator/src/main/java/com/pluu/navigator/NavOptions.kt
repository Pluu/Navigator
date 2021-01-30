package com.pluu.navigator

class NavOptions private constructor(
    builder: Builder
) {
    val singleTop = builder.singleTop

    data class Builder(
        var singleTop: Boolean = false
    ) {
        fun singleTop(singleTop: Boolean) = apply {
            this.singleTop = singleTop
        }

        fun build() = NavOptions(this)
    }
}

