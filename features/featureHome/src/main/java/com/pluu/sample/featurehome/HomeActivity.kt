package com.pluu.sample.featurehome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pluu.navigator.Navigator
import com.pluu.sample.featurehome.databinding.ActivityHomeBinding
import com.pluu.sample.routeconst.Routes1
import com.pluu.utils.showToast
import com.pluu.utils.viewBinding

class HomeActivity: AppCompatActivity(R.layout.activity_home) {
    private val binding by viewBinding(ActivityHomeBinding::bind)

    private val sampleRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGo.setOnClickListener {
            Navigator.of(this)
                .startForResult(Routes1.Feature1, sampleRequestCode)
        }

        binding.btnDeepLinkSimple.setOnClickListener {
            val executedDeepLink = Navigator.of(this)
                .execute("pluu://feature1")

            if (!executedDeepLink) {
                showToast("Undefined DeepLink")
            }
        }

        binding.btnDeepLinkSimpleQueryString.setOnClickListener {
            val executedDeepLink = Navigator.of(this)
                .execute("pluu://feature1/sample1?type=123")

            if (!executedDeepLink) {
                showToast("Undefined DeepLink")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == sampleRequestCode) {
            showToast("Test")
        }
    }
}