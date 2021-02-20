package com.pluu.sample.feature2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.navigator.util.bindExtra
import com.pluu.navigator.util.optionalBindExtra
import com.pluu.sample.feature2.databinding.ActivityFeature2Binding
import com.pluu.sample.routeconst.SampleParam
import com.pluu.utils.viewBinding

class Feature2Activity : AppCompatActivity(R.layout.activity_feature2) {
    private val binding by viewBinding(ActivityFeature2Binding::bind)

    private val param by bindExtra<SampleParam>()

    private val param1 by bindExtra<SampleParam, Int> {
        it.value
    }

    private val param2 by optionalBindExtra<SampleParam, String> {
        it?.value2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.tvParam.text = """
            param = $param
            param.value1 = ${"%,d".format(param1)}
            param.value2 = $param2
        """.trimIndent()

        binding.btnFinish.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
    }
}