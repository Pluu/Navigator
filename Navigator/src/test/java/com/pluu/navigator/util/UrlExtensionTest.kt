package com.pluu.navigator.util

import org.junit.Assert.*
import org.junit.Test

class UrlExtensionTest {

    @Test
    fun hasScheme() {
        assertTrue("https://".hasScheme())
        assertTrue("https:".hasScheme())
        assertTrue("https://www.google.com".hasScheme())
        assertFalse("www.google.com".hasScheme())
    }

    @Test
    fun trimUriSeparator() {
        listOf(
            "https://www.google.com",
            "https://www.google.com/",
            "/https://www.google.com",
            "/https://www.google.com/"
        ).forEach {
            assertEquals("https://www.google.com", it.trimUriSeparator())
        }
    }
}