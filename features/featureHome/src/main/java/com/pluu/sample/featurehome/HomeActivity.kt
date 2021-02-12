package com.pluu.sample.featurehome

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
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
    }

    private fun initProviderSample() {
        binding.container.addText("Provider Pattern")

        binding.container.addLabel("Route")
        binding.container.addButton("Routes1.Feature1") {
            Navigator.of(this)
                .startForResult(Routes1.Feature1, sampleRequestCode)
        }
        binding.container.addLabel("Route with Parameter")
        binding.container.addButton("Routes2.Feature2 with SampleParam") {
            Navigator.of(this).start(
                route = Routes2.Feature2,
                param = SampleParam(
                    (0..100_000_000).random()
                )
            )
        }

        binding.container.addLabel("DeepLink > Simple")
        binding.container.addButton("pluu://feature1") {
            Navigator.of(this)
                .execute("pluu://feature1")
        }
        binding.container.addLabel("DeepLink > Relative Path")
        binding.container.addButton("pluu://feature1/sample1?type=123") {
            Navigator.of(this)
                .execute("pluu://feature1/sample1?type=123")
        }
        binding.container.addLabel("DeepLink > Command")
        binding.container.addButton("pluu://feature1/sample2?type=97531") {
            Navigator.of(this)
                .execute("pluu://feature1/sample2?type=97531")
        }
        binding.container.addLabel("DeepLink > Provider Command")
        binding.container.addButton("pluu://feature2/command?type=qwertyuiop") {
            Navigator.of(this)
                .execute("pluu://feature2/command?type=qwertyuiop")
        }
        binding.container.addLabel("DeepLink > Provider Command")
        binding.container.addButton("pluu://feature2/command/extension?type=abcd") {
            Navigator.of(this)
                .execute("pluu://feature2/command/extension?type=abcd")
        }
    }

    private fun initGraph() {
        binding.container.addText("Graph Pattern")

        binding.container.addLabel("Route")
        binding.container.addButton("Routes1.Feature1_Graph") {
            Navigator.of(this)
                .start(Routes1.Feature1Graph)
        }
        binding.container.addLabel("DeepLink > Default Path")
        binding.container.addButton("pluu://feature1_graph") {
            Navigator.of(this)
                .execute("pluu://feature1_graph")
        }
        binding.container.addLabel("DeepLink > Relative Path")
        binding.container.addButton("pluu://feature1_graph/sample1?type=1834") {
            Navigator.of(this)
                .execute("pluu://feature1_graph/sample1?type=1834")
        }
        binding.container.addLabel("DeepLink > Sub Path")
        binding.container.addButton("pluu://feature1_graph/sub") {
            Navigator.of(this)
                .execute("pluu://feature1_graph/sub")
        }
        binding.container.addLabel("DeepLink > Command")
        binding.container.addButton("pluu://feature1_graph/sample2?type=13579") {
            Navigator.of(this)
                .execute("pluu://feature1_graph/sample2?type=13579")
        }
        binding.container.addLabel("DeepLink > Custom")
        binding.container.addButton("luckystar://izumi/konata") {
            Navigator.of(this)
                .execute("luckystar://izumi/konata")
        }
    }

    private fun initSubGraph() {
        binding.container.addText("SubGraph Pattern")

        binding.container.addLabel("Route")
        binding.container.addButton("Routes1.Feature1_Graph") {
            Navigator.of(this)
                .start(Routes1.Feature1Graph2)
        }
        binding.container.addLabel("DeepLink > Default Path")
        binding.container.addButton("pluu://feature1_1") {
            Navigator.of(this)
                .execute("pluu://feature1_1")
        }
        binding.container.addLabel("DeepLink > Relative Path")
        binding.container.addButton("pluu://feature1_1/sample1?type=582506") {
            Navigator.of(this)
                .execute("pluu://feature1_1/sample1?type=582506")
        }

        binding.container.addLabel("DeepLink > Custom")
        binding.container.addButton("luckystar://izumi/konata2") {
            Navigator.of(this)
                .execute("luckystar://izumi/konata2")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == sampleRequestCode) {
            showToast("Test")
        }
    }
}

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
        setTextColor(0xFF993300.toInt())
        setTypeface(null, Typeface.BOLD)
    }
    addView(view)
}

private fun LinearLayout.addLabel(text: String) {
    val view = TextView(context).apply {
        setText(text)
        textSize = 14f
        setTypeface(null, Typeface.BOLD)
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

private fun LinearLayout.addButton(text: String, action: () -> Unit) {
    val view = Button(context).apply {
        setText(text)
        isAllCaps = false
        setTypeface(null, Typeface.BOLD)
        setOnClickListener {
            action.invoke()
        }
    }
    addView(view)
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
