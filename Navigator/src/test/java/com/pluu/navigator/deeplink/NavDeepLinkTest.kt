package com.pluu.navigator.deeplink

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NavDeepLinkTest {

    @Test
    fun match() {
        val navDeepLink = NavDeepLink("pluu://test")
        assertTrue(navDeepLink.match("pluu://test"))
        assertTrue(navDeepLink.match("pluu://test?type=1"))

        // Fail
        assertFalse(navDeepLink.match("pluu://test/test"))
        assertFalse(navDeepLink.match("pluu://test/test?type=1"))
    }

    @Test
    fun matchingArguments_Path() {
        val navDeepLink = NavDeepLink("pluu://test/{id}")

        // Not Matching
        navDeepLink.easyMatchingArguments("blablabla", false)
        navDeepLink.easyMatchingArguments("pluu://test", false)
        navDeepLink.easyMatchingArguments("pluu://test?type=abc", false)

        // Matching
        navDeepLink.easyMatchingArguments("pluu://test/1", true) {
            assertEntry(mapOf<String, Any>("id" to "1"), it)
        }
        navDeepLink.easyMatchingArguments("pluu://test/1?type=abc", true) {
            assertEntry(mapOf<String, Any>("id" to "1"), it)
        }
    }

    @Test
    fun matchingArguments_NonQueryString() {
        val navDeepLink = NavDeepLink("pluu://test")

        // Not Matching
        navDeepLink.easyMatchingArguments("blablabla", false)

        // Matching
        navDeepLink.easyMatchingArguments("pluu://test", true) {
            assertEntry(emptyMap(), it)
        }
        navDeepLink.easyMatchingArguments("pluu://test?type=1", true) {
            assertEntry(emptyMap(), it)
        }
    }

    @Test
    fun matchingArguments_QueryString() {
        val navDeepLink = NavDeepLink("pluu://test?type={type}")

        // Not Matching
        navDeepLink.easyMatchingArguments("blablabla", false)

        // Matching
        navDeepLink.easyMatchingArguments("pluu://test", true) {
            assertEntry(emptyMap(), it)
        }
        navDeepLink.easyMatchingArguments("pluu://test?none=1", true) {
            assertEntry(emptyMap(), it)
        }
        navDeepLink.easyMatchingArguments("pluu://test?type=abc", true) {
            assertEntry(mapOf<String, Any>("type" to "abc"), it)
        }
        navDeepLink.easyMatchingArguments("pluu://test?abc=type&type=abc", true) {
            assertEntry(mapOf<String, Any>("type" to "abc"), it)
        }
    }

    @Test
    fun matchingArguments_Multiple_QueryString() {
        val navDeepLink = NavDeepLink("pluu://test?arg1={arg1}&arg2={arg2}")

        // Not Matching
        navDeepLink.easyMatchingArguments("blablabla", false)

        // Matching
        navDeepLink.easyMatchingArguments("pluu://test", true) {
            assertEntry(emptyMap(), it)
        }
        navDeepLink.easyMatchingArguments("pluu://test?none=1", true) {
            assertEntry(emptyMap(), it)
        }
        navDeepLink.easyMatchingArguments("pluu://test?arg1=abc", true) {
            assertEntry(mapOf<String, Any>("arg1" to "abc"), it)
        }
        navDeepLink.easyMatchingArguments("pluu://test?arg2=abc", true) {
            assertEntry(mapOf<String, Any>("arg2" to "abc"), it)
        }
        navDeepLink.easyMatchingArguments("pluu://test?arg1=abc&arg2=def", true) {
            assertEntry(mapOf<String, Any>("arg1" to "abc", "arg2" to "def"), it)
        }
        navDeepLink.easyMatchingArguments("pluu://test?arg2=abc&arg1=def", true) {
            assertEntry(mapOf<String, Any>("arg2" to "abc", "arg1" to "def"), it)
        }
    }

    @Test
    fun matchingArguments_path_Multiple_QueryString() {
        val navDeepLink = NavDeepLink("pluu://test/{id}?arg1={arg1}&arg2={arg2}")

        // Not Matching
        navDeepLink.easyMatchingArguments("blablabla", false)

        // Matching
        navDeepLink.easyMatchingArguments("pluu://test", false)
        navDeepLink.easyMatchingArguments("pluu://test?none=1", false)
        navDeepLink.easyMatchingArguments("pluu://test/qwe", true) {
            assertEntry(mapOf<String, Any>("id" to "qwe"), it)
        }
        navDeepLink.easyMatchingArguments("pluu://test/qwe?arg1=abc", true) {
            assertEntry(mapOf<String, Any>("id" to "qwe", "arg1" to "abc"), it)
        }
        navDeepLink.easyMatchingArguments("pluu://test/qwe?arg2=abc", true) {
            assertEntry(mapOf<String, Any>("id" to "qwe", "arg2" to "abc"), it)
        }
        navDeepLink.easyMatchingArguments("pluu://test/qwe?arg1=abc&arg2=def", true) {
            assertEntry(mapOf<String, Any>("id" to "qwe", "arg1" to "abc", "arg2" to "def"), it)
        }
        navDeepLink.easyMatchingArguments("pluu://test/qwe?arg2=abc&arg1=def", true) {
            assertEntry(mapOf<String, Any>("id" to "qwe", "arg2" to "abc", "arg1" to "def"), it)
        }
    }

    private fun NavDeepLink.easyMatchingArguments(
        link: String,
        isMatched: Boolean,
        argsCheck: (Map<String, Any>) -> Unit = {}
    ) {
        val result = matchingArguments(link)
        if (isMatched) {
            requireNotNull(result)
            argsCheck(result)
        } else {
            assertNull(result)
        }
    }

    private fun assertEntry(expected: Map<String, Any>, actual: Map<String, Any>) {
        assertEquals(expected.size, actual.size)
        expected.forEach { (key, value) ->
            assertTrue(actual.containsKey(key))
            assertEquals(actual[key], value)
        }
    }
}