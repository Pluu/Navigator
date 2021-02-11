package com.pluu.sample.feature2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.pluu.sample.feature2.databinding.ActivityFeature2SubBinding
import com.pluu.utils.viewBinding

class Feature2SubActivity : AppCompatActivity(R.layout.activity_feature2_sub) {
    private val binding by viewBinding(ActivityFeature2SubBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent.extras?.let { extras ->
            binding.tvParam.text = extras.keySet()
                .joinToString(separator = System.lineSeparator()) { key ->
                    "$key : ${extras.get(key)}"
                }
        }

        binding.receiveLayout.isGone = intent.extras?.isEmpty ?: true
    }
}