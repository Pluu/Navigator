package com.pluu.sample.feature1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.navigator.Navigator
import com.pluu.sample.feature1.databinding.ActivityFeature1Binding
import com.pluu.sample.routeconst.Routes2
import com.pluu.sample.routeconst.SampleParam
import com.pluu.utils.viewBinding

class Feature1Activity : AppCompatActivity(R.layout.activity_feature1) {
    private val binding by viewBinding(ActivityFeature1Binding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGo.setOnClickListener {
            Navigator.of(this).start(
                route = Routes2.Feature2,
                param = SampleParam(
                    (0..100_000_000).random()
                )
            )
        }

        binding.btnDeepLink.setOnClickListener {
            Navigator.of(this)
                .execute("pluu://feature2")
        }

        binding.btnFinish.setOnClickListener {
            finish()
        }
    }
}