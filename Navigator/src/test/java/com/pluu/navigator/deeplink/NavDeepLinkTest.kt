package com.pluu.navigator.deeplink

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Config.OLDEST_SDK])
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

    private fun NavDeepLink.easyMatchingArguments(
        link: String,
        isNonNull: Boolean,
        argSize: Int = 0
    ) {
        val result = matchingArguments(link)
        if (isNonNull) {
            requireNotNull(result)
            assertEquals(argSize, result.size)
        } else {
            assertNull(result)
        }
    }

    @Test
    fun matchingArguments_Path() {
        val navDeepLink = NavDeepLink("pluu://test/{id}")

        // Not Matching
        navDeepLink.easyMatchingArguments("blablabla", false)
        navDeepLink.easyMatchingArguments("pluu://test", false)
        navDeepLink.easyMatchingArguments("pluu://test?type=abc", false)

        // Matching
        navDeepLink.easyMatchingArguments("pluu://test/1", true, 1)
        navDeepLink.easyMatchingArguments("pluu://test/1?type=abc", true, 1)
    }

    @Test
    fun matchingArguments_NonQueryString() {
        val navDeepLink = NavDeepLink("pluu://test")

        // Not Matching
        navDeepLink.easyMatchingArguments("blablabla", false)

        // Matching
        navDeepLink.easyMatchingArguments("pluu://test", true, 0)
        navDeepLink.easyMatchingArguments("pluu://test?type=1", true, 0)
    }

    @Test
    fun matchingArguments_QueryString() {
        val navDeepLink = NavDeepLink("pluu://test?type={type}")

        // Not Matching
        navDeepLink.easyMatchingArguments("blablabla", false)

        // Matching
        navDeepLink.easyMatchingArguments("pluu://test", true, 0)
        navDeepLink.easyMatchingArguments("pluu://test?none=1", true, 0)
        navDeepLink.easyMatchingArguments("pluu://test?type=abc", true, 1)
        navDeepLink.easyMatchingArguments("pluu://test?abc=type&type=abc", true, 1)
    }

    @Test
    fun matchingArguments_Multiple_QueryString() {
        val navDeepLink = NavDeepLink("pluu://test?arg1={arg1}&arg2={arg2}")

        // Not Matching
        navDeepLink.easyMatchingArguments("blablabla", false)

        // Matching
        navDeepLink.easyMatchingArguments("pluu://test", true, 0)
        navDeepLink.easyMatchingArguments("pluu://test?none=1", true, 0)
        navDeepLink.easyMatchingArguments("pluu://test?arg1=abc", true, 1)
        navDeepLink.easyMatchingArguments("pluu://test?arg2=abc", true, 1)
        navDeepLink.easyMatchingArguments("pluu://test?arg1=abc&arg2=abc", true, 2)
        navDeepLink.easyMatchingArguments("pluu://test?arg2=abc&arg1=abc", true, 2)
    }
}