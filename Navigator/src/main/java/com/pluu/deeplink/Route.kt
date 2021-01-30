package com.pluu.deeplink

abstract class AbstractRoute(
    override val path: String
) : Destination {
    fun register(executor: LINK_EXECUTOR) {
        DeepLinker.registerRoute(this, executor)
    }
}

class DeepLink(
    deepLinkPath: String
) : AbstractRoute(deepLinkPath)
