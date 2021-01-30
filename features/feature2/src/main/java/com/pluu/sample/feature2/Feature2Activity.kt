package com.pluu.sample.feature2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.sample.feature2.databinding.ActivityFeature2Binding
import com.pluu.utils.ExtraDelegate
import com.pluu.utils.viewBinding

class Feature2Activity : AppCompatActivity(R.layout.activity_feature2) {
    private val binding by viewBinding(ActivityFeature2Binding::bind)

    private val param1 by ExtraDelegate<Int>("test_key")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.tvParam.text = "%,d".format(param1)

        binding.btnFinish.setOnClickListener {
            finish()
        }
    }
}