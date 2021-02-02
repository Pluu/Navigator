package com.pluu.sample.feature2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.navigator.ROUTE_PARAMS_KEY
import com.pluu.sample.feature2.databinding.ActivityFeature2Binding
import com.pluu.sample.routeconst.SampleParam
import com.pluu.utils.ExtraDelegate
import com.pluu.utils.viewBinding

class Feature2Activity : AppCompatActivity(R.layout.activity_feature2) {
    private val binding by viewBinding(ActivityFeature2Binding::bind)

    private val param1 by ExtraDelegate<SampleParam>(ROUTE_PARAMS_KEY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.tvParam.text = "%,d".format(param1.value)

        binding.btnFinish.setOnClickListener {
            finish()
        }
    }
}