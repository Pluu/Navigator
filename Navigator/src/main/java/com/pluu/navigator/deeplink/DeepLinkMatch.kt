package com.pluu.navigator.deeplink

import com.pluu.navigator.Destination

class DeepLinkMatch(
    val request: DeepLinkRequest,
    val destination: Destination,
    val args: Map<String, Any?>,
    val isExactDeepLink: Boolean
)