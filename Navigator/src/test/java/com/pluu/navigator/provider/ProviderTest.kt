package com.pluu.navigator.provider

import android.content.Intent
import androidx.core.net.toUri
import com.pluu.navigator.*
import com.pluu.navigator.deeplink.DeepLinkMatchResult
import com.pluu.navigator.deeplink.DeepLinkRequest
import com.pluu.navigator.starter.Starter
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ProviderTest {

    private val coreGraph = Navigator.coreGraph

    @Before
    fun setUp() {
        coreGraph.clear()
    }

    @Test
    fun testRouteProvider() {
        val direction = object : Direction() {}

        // Before, action provide
        assertFalse(coreGraph.containsDestination(direction))

        // Destination Provider
        val intent = Intent("Test")
        val provider = routeProvider(direction) {
            intent
        }
        // Check if it is not registered even if provide is defined
        assertFalse(coreGraph.containsDestination(direction))

        // Execute provide
        provider.provide()

        // After, action provide
        assertTrue(coreGraph.containsDestination(direction))
        val actualRouting = coreGraph.getRequiredRouting(direction) as? CreateRouting
        checkNotNull(actualRouting)

        val starter = mock(Starter::class.java)
        val actualIntent = actualRouting.createIntent(starter)
        assertEquals("Test", actualIntent.action)
    }

    @Test
    fun testUnProvideRouteProvider() {
        val direction = object : Direction() {}
        // Unregiste direction
        assertFalse(coreGraph.containsDestination(direction))

        val intent = Intent("Test")
        routeProvider(direction) {
            intent
        }

        // Un action provide
        assertFalse(coreGraph.containsDestination(direction))
    }

    @Test
    fun testDeepLinkProvider() {
        val testDeepLink = "test://abc.def"

        val deepLink = DeepLink(testDeepLink)
        // Before, deeplink provide
        assertFalse(coreGraph.containsDestination(deepLink))

        @Suppress("UNCHECKED_CAST")
        val mockExecutor = mock(
            Function2::class.java as Class<Function2<Starter, DeepLinkMatchResult, Unit>>
        )
        val provider = deepLinkProvider(testDeepLink, mockExecutor)
        // Check if it is not registered even if provide is defined
        assertFalse(coreGraph.containsDestination(deepLink))

        // Execute provide
        provider.provide()

        // After, action provide
        assertTrue(coreGraph.containsDestination(deepLink))

        val matchedDeepLink = coreGraph.matchDeepLink(DeepLinkRequest(testDeepLink.toUri()))
        checkNotNull(matchedDeepLink)
        val actualRouting =
            coreGraph.getRequiredRouting(matchedDeepLink.destination) as? ExecuteRouting
        checkNotNull(actualRouting)

        val starter = mock(Starter::class.java)
        actualRouting.execute(starter, matchedDeepLink)

        // Verify function
        verify(mockExecutor).invoke(starter, matchedDeepLink)
    }

    @Test
    fun testUnProvideDeepLinkProvider() {
        val testDeepLink = "test://abc.def"

        val deepLink = DeepLink(testDeepLink)
        // Before, deeplink provide
        assertFalse(coreGraph.containsDestination(deepLink))

        @Suppress("UNCHECKED_CAST")
        val mockExecutor = mock(
            Function2::class.java as Class<Function2<Starter, DeepLinkMatchResult, Unit>>
        )
        deepLinkProvider(testDeepLink, mockExecutor)

        assertFalse(coreGraph.containsDestination(deepLink))
    }

    @Test
    fun testDeepLinkCommandProvider() {
        val testDeepLink = "test://abc.def"

        val deepLink = DeepLink(testDeepLink)
        assertFalse(coreGraph.containsDestination(deepLink))

        // Define DeepLink Provider
        val testProvider = deepLinkProvider<TestDeepLinkCommand>(testDeepLink)

        // Check if it is not registered even if provide is defined
        assertFalse(coreGraph.containsDestination(deepLink))

        testProvider.provide()

        // After, action provide
        assertTrue(coreGraph.containsDestination(deepLink))

        val matchedDeepLink = coreGraph.matchDeepLink(DeepLinkRequest(testDeepLink.toUri()))
        checkNotNull(matchedDeepLink)
        val actualRouting =
            coreGraph.getRequiredRouting(matchedDeepLink.destination) as? CommandRouting<*>
        checkNotNull(actualRouting)

        assertEquals(
            TestDeepLinkCommand::class.java.canonicalName,
            actualRouting.command.canonicalName
        )
    }
}

private class TestDeepLinkCommand : DeepLinkCommand {
    override fun execute(starter: Starter) {
    }
}