package com.pluu.sample.feature2.navigator

import com.pluu.navigator.provider.Provider

///////////////////////////////////////////////////////////////////////////
// Sample Definition
///////////////////////////////////////////////////////////////////////////

val sample_feature2_pattern: List<Provider> = listOf(
    DeepLinkProvider(),
    DeepLinkProviderForJava(),
    Feature1_Route_1,
    RouteProviderForJava()
)