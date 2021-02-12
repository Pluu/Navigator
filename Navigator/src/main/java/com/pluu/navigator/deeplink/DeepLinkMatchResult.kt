package com.pluu.navigator.deeplink

import com.pluu.navigator.Destination

/**
 * Wrapper class with matching result information according to request
 *
 * @property request Requested DeepLink information
 * @property destination Matched destination information
 * @property args Parameter information defined in {type} format
 */
class DeepLinkMatchResult(
    val request: DeepLinkRequest,
    val destination: Destination,
    val args: Map<String, Any?>,
)
