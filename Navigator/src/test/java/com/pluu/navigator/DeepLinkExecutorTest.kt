package com.pluu.navigator

import com.pluu.navigator.deeplink.DeepLinkMatchResult
import com.pluu.navigator.starter.Starter
import com.pluu.navigator.util.mock
import com.pluu.navigator.util.whenever
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DeepLinkExecutorTest {

    private val deepLinkExecutor = DeepLinkExecutor()

    @Mock
    lateinit var starter: Starter

    @Mock
    lateinit var deepLinkMatchResult: DeepLinkMatchResult

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testUnknownExecute() {
        val routing = object : UnknownRouting {}
        val result = deepLinkExecutor.execute(routing, starter, deepLinkMatchResult)
        assertFalse(result)
    }

    @Test
    fun testExecuteRouting() {
        val routing = mock<ExecuteRouting>()
        val result = deepLinkExecutor.execute(routing, starter, deepLinkMatchResult)
        assertTrue(result)

        verify(routing).execute(starter, deepLinkMatchResult)
    }

    @Test
    fun testCommandRouting() {
        val routing = CommandRouting(TestDeepLinkCommand::class.java)

        whenever(deepLinkMatchResult.args)
            .thenReturn(
                mapOf(
                    "arg1" to "abcd",
                    "arg2" to 1234
                )
            )

        val result = deepLinkExecutor.execute(routing, starter, deepLinkMatchResult)
        assertTrue(result)

        // Check argument
        assertEquals(1, InvokeResult.invokeDeepLinkCommandCount)
        assertEquals("abcd", InvokeResult.invokeDeepLinkCommand_arg1)
        assertEquals(1234, InvokeResult.invokeDeepLinkCommand_arg2)
    }

    interface UnknownRouting : AbstractExecutor

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
}

