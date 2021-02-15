package com.pluu.sample.featurehome

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import com.pluu.navigator.Navigator
import com.pluu.sample.featurehome.databinding.ActivityHomeBinding
import com.pluu.sample.routeconst.Routes1
import com.pluu.sample.routeconst.Routes2
import com.pluu.sample.routeconst.SampleParam
import com.pluu.utils.dp2px
import com.pluu.utils.showToast
import com.pluu.utils.viewBinding

class HomeActivity : AppCompatActivity(R.layout.activity_home) {
    private val binding by viewBinding(ActivityHomeBinding::bind)

    private val sampleRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.container.addTitle("Navigator Sample")
        initProviderSample()
        binding.container.addDivider()
        initGraph()
        binding.container.addDivider()
        initSubGraph()
        binding.container.addDivider()
        initJava()
    }

    private fun initProviderSample() {
        binding.container.addText("Provider Pattern")

        binding.container.addRoute(
            label = "Route",
            text = "Routes1.Feature1"
        ) {
            Navigator.of(this)
                .startForResult(Routes1.Feature1, sampleRequestCode)
        }
        binding.container.addRoute(
            label = "Route with Parameter",
            text = "Routes2.Feature2 with SampleParam(123_456_789)"
        ) {
            Navigator.of(this).start(
                direction = Routes2.Feature2,
                param = SampleParam(123_456_789)
            )
        }

        binding.container.addDeepLink(
            label = "DeepLink > Simple",
            format = "pluu://feature1",
            text = "pluu://feature1"
        ) {
            Navigator.of(this)
                .execute("pluu://feature1")
        }
        binding.container.addDeepLink(
            label = "DeepLink > Relative Path",
            format = "pluu + feature1/sample1?type={type}",
            text = "pluu://feature1/sample1?type=123"
        ) {
            Navigator.of(this)
                .execute("pluu://feature1/sample1?type=123")
        }
        binding.container.addDeepLink(
            label = "DeepLink > Command",
            format = "pluu://feature1/sample2?type={value}",
            text = "pluu://feature1/sample2?type=97531"
        ) {
            Navigator.of(this)
                .execute("pluu://feature1/sample2?type=97531")
        }
        binding.container.addDeepLink(
            label = "DeepLink > Provider Command",
            format = "pluu://feature2/command?type={type}",
            text = "pluu://feature2/command?type=qwertyuiop"
        ) {
            Navigator.of(this)
                .execute("pluu://feature2/command?type=qwertyuiop")
        }
        binding.container.addDeepLink(
            label = "DeepLink > Provider Command",
            format = "pluu://feature2/command/extension?type={type}",
            text = "pluu://feature2/command/extension?type=abcd"
        ) {
            Navigator.of(this)
                .execute("pluu://feature2/command/extension?type=abcd")
        }

        binding.container.addDeepLink(
            label = "DeepLink > Path",
            format = "pluu://featurePath/{id}/info?arg1={arg1}&arg2={arg2}",
            text = "pluu://featurePath/pluu/info"
        ) {
            Navigator.of(this)
                .execute("pluu://featurePath/pluu/info")
        }
        binding.container.addDeepLink(
            label = "DeepLink > Path & Arguments",
            format = "pluu://featurePath/{id}/info?arg1={arg1}&arg2={arg2}",
            text = "pluu://featurePath/pluu/info?arg1=abc&arg2=def"
        ) {
            Navigator.of(this)
                .execute("pluu://featurePath/pluu/info?arg1=abc&arg2=def")
        }
    }

    private fun initGraph() {
        binding.container.addText("Graph Pattern")

        binding.container.addRoute(
            label = "Route",
            text = "Routes1.Feature1_Graph"
        ) {
            Navigator.of(this)
                .start(Routes1.Feature1Graph)
        }
        binding.container.addRoute(
            label = "DeepLink > Default Path",
            text = "pluu://feature1_graph"
        ) {
            Navigator.of(this)
                .execute("pluu://feature1_graph")
        }
        binding.container.addDeepLink(
            label = "DeepLink > Relative Path",
            format = "pluu + feature1_graph + /sample1?type={type}",
            text = "pluu://feature1_graph/sample1?type=1834"
        ) {
            Navigator.of(this)
                .execute("pluu://feature1_graph/sample1?type=1834")
        }
        binding.container.addDeepLink(
            label = "DeepLink > Sub Path",
            format = "pluu + feature1_graph + sub",
            text = "pluu://feature1_graph/sub"
        ) {
            Navigator.of(this)
                .execute("pluu://feature1_graph/sub")
        }
        binding.container.addDeepLink(
            label = "DeepLink > Command",
            format = "pluu + feature1_graph + sample2?type={value}",
            text = "pluu://feature1_graph/sample2?type=13579"
        ) {
            Navigator.of(this)
                .execute("pluu://feature1_graph/sample2?type=13579")
        }
        binding.container.addDeepLink(
            label = "DeepLink > Custom",
            format = "luckystar://izumi/konata",
            text = "luckystar://izumi/konata"
        ) {
            Navigator.of(this)
                .execute("luckystar://izumi/konata")
        }
    }

    private fun initSubGraph() {
        binding.container.addText("SubGraph Pattern")

        binding.container.addRoute(
            label = "Route",
            text = "Routes1.Feature1_Graph"
        ) {
            Navigator.of(this)
                .start(Routes1.Feature1Graph2)
        }
        binding.container.addDeepLink(
            label = "DeepLink > Default Path",
            format = "pluu + feature1_1 + /",
            text = "pluu://feature1_1"
        ) {
            Navigator.of(this)
                .execute("pluu://feature1_1")
        }
        binding.container.addDeepLink(
            label = "DeepLink > Relative Path",
            format = "pluu + feature1_1 + /sample1?type={type}",
            text = "pluu://feature1_1/sample1?type=582506"
        ) {
            Navigator.of(this)
                .execute("pluu://feature1_1/sample1?type=582506")
        }

        binding.container.addDeepLink(
            label = "DeepLink > Custom",
            format = "luckystar://izumi/konata2",
            text = "luckystar://izumi/konata2"
        ) {
            Navigator.of(this)
                .execute("luckystar://izumi/konata2")
        }
    }

    private fun initJava() {
        binding.container.addText("Java Pattern")

        binding.container.addRoute(
            label = "Route",
            text = "Routes2.Feature2ForJava with SampleParam(987_654_321)"
        ) {
            Navigator.of(this)
                .start(
                    direction = Routes2.Feature2ForJava,
                    param = SampleParam(987_654_321)
                )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == sampleRequestCode) {
            showToast("Test")
        }
    }
}

///////////////////////////////////////////////////////////////////////////
// Simple View
///////////////////////////////////////////////////////////////////////////

private fun LinearLayout.addTitle(text: String) {
    val view = TextView(context).apply {
        setText(text)
        textSize = 24f
        setTypeface(null, Typeface.BOLD)
    }
    addView(view,
        LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = context.dp2px(20f)
        }
    )
}

private fun LinearLayout.addText(text: String) {
    val view = TextView(context).apply {
        setText(text)
        textSize = 20f
        setTextColor(0xFFE71D36.toInt())
        setTypeface(null, Typeface.BOLD)
    }
    addView(view)
}

private fun LinearLayout.addLabel(
    text: String,
    @ColorInt textColor: Int? = null
) {
    val view = TextView(context).apply {
        setText(text)
        textSize = 14f
        setTypeface(null, Typeface.BOLD)
        if (textColor != null) {
            setTextColor(textColor)
        }
    }
    addView(
        view,
        LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            topMargin = context.dp2px(5f)
        }
    )
}

private fun LinearLayout.addRoute(
    label: String,
    text: String, action: () -> Unit
) {
    addLabel(text = label, textColor = 0xFF42A5F5.toInt())
    addView(
        Button(context).apply {
            setText(text)
            isAllCaps = false
            setTypeface(null, Typeface.BOLD)
            setOnClickListener {
                action.invoke()
            }
        }
    )
}

private fun LinearLayout.addDeepLink(
    label: String,
    format: String,
    text: String,
    action: () -> Unit
) {
    addLabel(text = label, textColor = 0xFF42A5F5.toInt())
    addLabel(format)
    addView(
        Button(context).apply {
            setText(text)
            isAllCaps = false
            setTypeface(null, Typeface.BOLD)
            setOnClickListener {
                action.invoke()
            }
        }
    )
}

private fun LinearLayout.addDivider() {
    val view = View(context).apply {
        setBackgroundColor(0xFF00BCD4.toInt())
    }
    addView(
        view,
        LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            context.dp2px(2f)
        ).apply {
            topMargin = context.dp2px(15f)
            bottomMargin = context.dp2px(15f)
        }
    )
}
