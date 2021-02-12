package com.pluu.navigator.deeplink

import com.pluu.navigator.Destination

/**
 * 매칭된 결과를 전달하는 용도의 Wrapper Class
 *
 * @property request 요청한 DeepLink 정보
 * @property destination 매칭된 Destination 정보
 * @property args {type} 형태로 정의된 QueryString Parameter 정보
 */
class DeepLinkMatchResult(
    val request: DeepLinkRequest,
    val destination: Destination,
    val args: Map<String, Any?>,
)
