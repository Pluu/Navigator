package com.pluu.navigator.starter

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.core.net.toUri
import com.google.common.collect.Maps
import com.pluu.navigator.DeepLinkCommand
import com.pluu.navigator.DeepLinkExecutor
import com.pluu.navigator.Direction
import com.pluu.navigator.DirectionParam
import com.pluu.navigator.DirectionWithParam
import com.pluu.navigator.RouteGraph
import com.pluu.navigator.deeplink.DeepLinkMatchResult
import com.pluu.navigator.deeplink.DeepLinkRequest
import com.pluu.navigator.exception.MissingRouteThrowable
import com.pluu.navigator.util.argumentCaptor
import com.pluu.navigator.util.capture
import com.pluu.navigator.util.findDirectionParam
import com.pluu.navigator.util.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NavigatorStarterTest {

    @Mock
    lateinit var starter: Starter

    lateinit var graph: RouteGraph

    private val deepLinkExecutor = DeepLinkExecutor()

    lateinit var navigatorStarter: NavigatorStarter

    private object TestDirection {
        object Test1 : Direction()
        object Test2 : DirectionWithParam<TestWithParam>()
    }

    private var invokeDeepLinkResult: DeepLinkMatchResult? = null

    private object InvokeResult {
        var invokeDeepLinkCommandCount = 0
        var invokeDeepLinkCommand_arg1: String? = null
        var invokeDeepLinkCommand_arg2: Int? = null
    }

    private class TestDeepLinkCommand(
        private val arg1: String,
        private val arg2: Int
    ) : DeepLinkCommand {
        override fun execute(starter: Starter) {
            InvokeResult.invokeDeepLinkCommandCount++
            InvokeResult.invokeDeepLinkCommand_arg1 = arg1
            InvokeResult.invokeDeepLinkCommand_arg2 = arg2
        }
    }

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        invokeDeepLinkResult = null
        InvokeResult.invokeDeepLinkCommandCount = 0
        InvokeResult.invokeDeepLinkCommand_arg1 = null
        InvokeResult.invokeDeepLinkCommand_arg2 = null

        graph = RouteGraph.Builder("test")
            .addDestination(TestDirection.Test1) { Intent("action TestDirection.Test1") }
            .addDestination(TestDirection.Test2) { Intent("action TestDirection.Test2") }
            .addDeepLink("pluu://test") { _, result ->
                invokeDeepLinkResult = result
            }
            .addDeepLink("pluu://test2?arg1={arg1}&arg2={arg2}") { _, result ->
                invokeDeepLinkResult = result
            }
            .addDeepLink("pluu://test3?arg1={arg1}&arg2={arg2}", TestDeepLinkCommand::class.java)
            .build()

        whenever(starter.validStarter())
            .thenReturn(true)

        navigatorStarter = NavigatorStarter(starter, graph, deepLinkExecutor)
    }

    @Test(expected = MissingRouteThrowable::class)
    fun testNonExistDirectionStart() {
        val testDirection = object : Direction() {}
        navigatorStarter.start(testDirection)
    }

    @Test
    fun testDirectionStart() {
        navigatorStarter.start(TestDirection.Test1)

        // Check argument
        val intentCaptor = argumentCaptor<Intent>()
        verify(starter).start(capture(intentCaptor))

        val actualIntent = intentCaptor.value
        assertEquals("action TestDirection.Test1", actualIntent.action)
        assertNull(actualIntent.extras)
    }

    @Test
    fun testDirectionWithParamStart() {
        navigatorStarter.start(TestDirection.Test2, TestWithParam("123", 456))

        // Check argument
        val intentCaptor = argumentCaptor<Intent>()
        verify(starter).start(capture(intentCaptor))

        val actualIntent = intentCaptor.value
        assertEquals("action TestDirection.Test2", actualIntent.action)
        val actualParam = actualIntent.extras!!.findDirectionParam<TestWithParam>()
        assertEquals("123", actualParam.p1)
        assertEquals(456, actualParam.p2)
    }

    @Test
    fun testDirectionStartForResult() {
        navigatorStarter.startForResult(TestDirection.Test1, 1234)

        // Check argument
        val intentCaptor = argumentCaptor<Intent>()
        val requestCodeCaptor = argumentCaptor<Int>()
        verify(starter).startForResult(capture(intentCaptor), capture(requestCodeCaptor))

        val actualIntent = intentCaptor.value
        assertEquals("action TestDirection.Test1", actualIntent.action)
        assertNull(actualIntent.extras)
        assertEquals(1234, requestCodeCaptor.value)
    }

    @Test
    fun testDirectionWithParamStartForResult() {
        navigatorStarter.startForResult(TestDirection.Test2, 1234, TestWithParam("123", 456))

        // Check argument
        val intentCaptor = argumentCaptor<Intent>()
        val requestCodeCaptor = argumentCaptor<Int>()
        verify(starter).startForResult(capture(intentCaptor), capture(requestCodeCaptor))

        val actualIntent = intentCaptor.value
        assertEquals("action TestDirection.Test2", actualIntent.action)
        val actualParam = actualIntent.extras!!.findDirectionParam<TestWithParam>()
        assertEquals("123", actualParam.p1)
        assertEquals(456, actualParam.p2)
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

        navigatorStarter.startForResult(TestDirection.Test1, resultLauncher)

        // Check argument
        assertEquals(1, resultLauncher.invokeCount)

        val invokeIntent = resultLauncher.invokeIntent
        checkNotNull(invokeIntent)
        assertEquals("action TestDirection.Test1", invokeIntent.action)
    }

    @Test
    fun testExecuteOnUnmatched() {
        val runResult = navigatorStarter.execute("test://test")
        assertFalse(runResult)

        val runResult2 = navigatorStarter.execute(DeepLinkRequest("test://test".toUri()))
        assertFalse(runResult2)
    }

    @Test
    fun testExecute() {
        val runResult = navigatorStarter.execute("pluu://test")
        assertTrue(runResult)

        // Check argument
        val actualDeepLinkMatchResult = invokeDeepLinkResult
        checkNotNull(actualDeepLinkMatchResult)
        assertEquals("pluu://test", actualDeepLinkMatchResult.request.uri.toString())
        assertEquals("pluu://test", actualDeepLinkMatchResult.destination.path)
        assertTrue(actualDeepLinkMatchResult.args.isEmpty())
    }

    @Test
    fun testExecuteWithParam() {
        val runResult = navigatorStarter.execute("pluu://test2?arg1=abcd&arg2=1234")
        assertTrue(runResult)

        // Check argument
        val actualDeepLinkMatchResult = invokeDeepLinkResult
        checkNotNull(actualDeepLinkMatchResult)
        assertEquals(
            "pluu://test2?arg1=abcd&arg2=1234",
            actualDeepLinkMatchResult.request.uri.toString()
        )
        assertEquals(
            "pluu://test2?arg1={arg1}&arg2={arg2}",
            actualDeepLinkMatchResult.destination.path
        )

        val difference = Maps.difference(
            mapOf("arg1" to "abcd", "arg2" to "1234"),
            actualDeepLinkMatchResult.args
        )

        assertTrue(difference.areEqual())
    }

    @Test
    fun testExecuteCommand() {
        val runResult = navigatorStarter.execute("pluu://test3?arg1=abcd&arg2=1234")
        assertTrue(runResult)

        // Check argument
        assertEquals(1, InvokeResult.invokeDeepLinkCommandCount)
        assertEquals("abcd", InvokeResult.invokeDeepLinkCommand_arg1)
        assertEquals(1234, InvokeResult.invokeDeepLinkCommand_arg2)
    }
}

private class TestWithParam(
    val p1: String,
    val p2: Int,
) : DirectionParam()