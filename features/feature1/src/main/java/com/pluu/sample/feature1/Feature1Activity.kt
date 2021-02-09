package com.pluu.sample.feature1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.pluu.navigator.Navigator
import com.pluu.sample.feature1.databinding.ActivityFeature1Binding
import com.pluu.sample.routeconst.Routes2
import com.pluu.sample.routeconst.SampleParam
import com.pluu.utils.viewBinding

class Feature1Activity : AppCompatActivity(R.layout.activity_feature1) {
    private val binding by viewBinding(ActivityFeature1Binding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let { extras ->
            binding.tvParam.text = extras.keySet()
                .joinToString(separator = System.lineSeparator()) { key ->
                    "${key}:${extras.get(key)}"
                }
        }

        binding.receiveLayout.isGone = intent.extras?.isEmpty ?: true
    }
}