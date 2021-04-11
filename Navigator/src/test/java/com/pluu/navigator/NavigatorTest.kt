package com.pluu.navigator

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragment
import com.pluu.navigator.starter.ActivityStarter
import com.pluu.navigator.starter.FragmentStarter
import com.pluu.navigator.starter.Starter
import com.pluu.navigator.util.mock
import com.pluu.navigator.util.toRouting
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NavigatorTest {

    private lateinit var coreGraph: RouteGraph

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Navigator.reset()
        coreGraph = Navigator.coreGraph
    }

    @Test
    fun testAddDestination_Creator() {
        val testDestination = object : Direction() {}
        Navigator.addDestination(testDestination, mock<CREATOR_ACTION>())

        assertTrue(coreGraph.containsDestination(testDestination))
        assertFalse(coreGraph.containsDestination(object : Direction() {}))
    }

    @Test
    fun testAddDestination_CreatorWithParam() {
        val testDestination = object : DirectionWithParam<TestParam>() {}
        Navigator.addDestination(testDestination, mock<CREATOR_ACTION>())

        assertTrue(coreGraph.containsDestination(testDestination))
        assertFalse(coreGraph.containsDestination(object : Direction() {}))
    }

    @Test
    fun testAddDestination_Executor() {
        Navigator.addDestination(DeepLink("test://abc.def"), mock<EXECUTOR_ACTION>())

        assertTrue(coreGraph.containsDestination(DeepLink("test://abc.def")))
        assertFalse(coreGraph.containsDestination(DeepLink("test://qwe.rty")))
    }

    @Test
    fun testAddDestinationWithExecutor() {
        Navigator.addDestinationWithExecutor(DeepLink("test://abc.def"), mock())

        assertTrue(coreGraph.containsDestination(DeepLink("test://abc.def")))
        assertFalse(coreGraph.containsDestination(DeepLink("test://qwe.rty")))
    }

    @Test
    fun testAddDestinationWithExecutor_CommandExecutor() {
        Navigator.addDestinationWithExecutor(
            DeepLink("test://abc.def"),
            TestDeepLinkCommand::class.java.toRouting()
        )

        assertTrue(coreGraph.containsDestination(DeepLink("test://abc.def")))
        assertFalse(coreGraph.containsDestination(DeepLink("test://qwe.rty")))
    }

    @Test
    fun testAddDestinations() {
        val testDestination = object : DirectionWithParam<TestParam>() {}
        val testGraph = RouteGraph.Builder(graphName = "sample")
            .addDestination(testDestination, mock())
            .addDeepLink("test://abc.def", mock<EXECUTOR_ACTION>())
            .addDeepLink("test://command.com", TestDeepLinkCommand::class.java.toRouting())
            .build()
        Navigator.addDestinations(testGraph)

        assertTrue(coreGraph.containsDestination(testDestination))
        assertFalse(coreGraph.containsDestination(object : Direction() {}))
        assertTrue(coreGraph.containsDestination(DeepLink("test://abc.def")))
        assertTrue(coreGraph.containsDestination(DeepLink("test://command.com")))
        assertFalse(coreGraph.containsDestination(DeepLink("test://qwe.rty")))
    }

    @Test
    fun testNavigatorOf_Activity() {
        val controller = buildActivity(TestActivity::class.java).setup()
        val starter = Navigator.of(controller.get())
        assertTrue(starter.starter is ActivityStarter)
    }

    @Test
    fun testNavigatorOf_Fragment() {
        val scenario = launchFragment<TestFragment>()
        scenario.onFragment { fragment ->
            val starter = Navigator.of(fragment)
            assertTrue(starter.starter is FragmentStarter)
        }
    }
}

private class TestParam : DirectionParam()

private class TestDeepLinkCommand : DeepLinkCommand {
    override fun execute(starter: Starter) {
    }
}

class TestActivity : Activity()
class TestFragment : Fragment()