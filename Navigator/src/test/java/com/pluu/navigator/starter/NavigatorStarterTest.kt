package com.pluu.navigator.starter

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import com.pluu.navigator.CreateRouting
import com.pluu.navigator.DeepLinkExecutor
import com.pluu.navigator.Direction
import com.pluu.navigator.RouteGraph
import com.pluu.navigator.exception.MissingRouteThrowable
import com.pluu.navigator.util.argumentCaptor
import com.pluu.navigator.util.capture
import com.pluu.navigator.util.whenever
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NavigatorStarterTest {

    @Mock
    lateinit var starter: Starter

    @Mock
    lateinit var graph: RouteGraph

    @Mock
    lateinit var deepLinkExecutor: DeepLinkExecutor


    lateinit var navigatorStarter: NavigatorStarter

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        navigatorStarter = NavigatorStarter(starter, graph, deepLinkExecutor)
    }

    @Test(expected = MissingRouteThrowable::class)
    fun testNonExistDirectionStart() {
        val testDirection = object : Direction() {}

        whenever(graph.containsDestination(testDirection))
            .thenReturn(false)

        navigatorStarter.start(testDirection)
    }

    @Test
    fun testDirectionStart() {
        val expectedIntent = Intent("test_action")

        val testDirection = object : Direction() {}
        val createRouting = mock(CreateRouting::class.java)

        whenever(createRouting.createIntent(starter))
            .thenReturn(expectedIntent)
        whenever(graph.containsDestination(testDirection))
            .thenReturn(true)
        whenever(graph.getRequiredRouting(testDirection))
            .thenReturn(createRouting)
        whenever(starter.validStarter())
            .thenReturn(true)

        navigatorStarter.start(testDirection)

        // Check argument
        val intentCaptor = argumentCaptor<Intent>()
        verify(starter).start(capture(intentCaptor))

        assertEquals("test_action", intentCaptor.value.action)
    }

    @Test
    fun testDirectionStartForResult() {
        val expectedIntent = Intent("test_action")
        val expectedRequestCode = 1234

        val testDirection = object : Direction() {}
        val createRouting = mock(CreateRouting::class.java)

        whenever(createRouting.createIntent(starter))
            .thenReturn(expectedIntent)
        whenever(graph.containsDestination(testDirection))
            .thenReturn(true)
        whenever(graph.getRequiredRouting(testDirection))
            .thenReturn(createRouting)
        whenever(starter.validStarter())
            .thenReturn(true)

        navigatorStarter.startForResult(testDirection, expectedRequestCode)

        // Check argument
        val intentCaptor = argumentCaptor<Intent>()
        val requestCodeCaptor = argumentCaptor<Int>()
        verify(starter).startForResult(capture(intentCaptor), capture(requestCodeCaptor))

        assertEquals("test_action", intentCaptor.value.action)
        assertEquals(1234, requestCodeCaptor.value)
    }

    @Test
    fun testDirectionStartForResultLauncher() {
        val resultLauncher = object : ActivityResultLauncher<Intent>() {
            var invokeCount = 0
            var invokeIntent: Intent? = null
            var invokeOptions: ActivityOptionsCompat? = null

            override fun launch(input: Intent?, options: ActivityOptionsCompat?) {
                invokeCount++
                invokeIntent = input
                invokeOptions = options
            }

            override fun unregister() {
            }

            override fun getContract(): ActivityResultContract<Intent, *> {
                return ActivityResultContracts.StartActivityForResult()
            }
        }

        val expectedIntent = Intent("test_action")

        val testDirection = object : Direction() {}
        val createRouting = mock(CreateRouting::class.java)

        whenever(createRouting.createIntent(starter))
            .thenReturn(expectedIntent)
        whenever(graph.containsDestination(testDirection))
            .thenReturn(true)
        whenever(graph.getRequiredRouting(testDirection))
            .thenReturn(createRouting)
        whenever(starter.validStarter())
            .thenReturn(true)

        navigatorStarter.startForResult(testDirection, resultLauncher)

        // Check argument
        assertEquals(
            "the registry was not invoked",
            1,
            resultLauncher.invokeCount
        )

        val invokeIntent = resultLauncher.invokeIntent
        checkNotNull(invokeIntent)
        assertEquals(
            "the registry was not invoked",
            "test_action",
            invokeIntent.action
        )
    }
}
