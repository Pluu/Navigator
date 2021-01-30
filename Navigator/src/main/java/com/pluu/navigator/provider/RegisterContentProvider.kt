package com.pluu.navigator.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.net.Uri

abstract class Provider : EmptyProvider() {
    abstract fun provide()

    override fun onCreate(): Boolean {
        provide()
        return true
    }
}

abstract class EmptyProvider : ContentProvider() {
    final override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ) = throw Exception("unused")

    final override fun insert(
        uri: Uri,
        values: ContentValues?
    ) = throw Exception("unused")

    final override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        throw Exception("unused")
    }

    final override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ) = throw Exception("unused")


    final override fun getType(
        uri: Uri
    ) = throw Exception("unused")
}