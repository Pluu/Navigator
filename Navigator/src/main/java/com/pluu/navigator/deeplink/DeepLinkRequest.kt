package com.pluu.navigator.deeplink

import android.net.Uri

class DeepLinkRequest(
    val uri: Uri
) {
    override fun toString(): String {
        return "DeepLinkRequest(uri='$uri')"
    }
}