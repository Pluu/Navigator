package com.pluu.sample.router

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.navigator.Navigator
import com.pluu.sample.routeconst.Routes1
import com.pluu.sample.router.databinding.ActivityMainBinding
import com.pluu.utils.showToast
import com.pluu.utils.viewBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding by viewBinding(ActivityMainBinding::bind)

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