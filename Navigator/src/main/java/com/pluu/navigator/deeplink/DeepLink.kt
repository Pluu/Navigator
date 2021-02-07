package com.pluu.navigator.deeplink

import com.pluu.navigator.AbstractRoute

class DeepLink(
    deepLinkPath: String
) : AbstractRoute() {
    init {
        setPath(deepLinkPath)
    }
}
