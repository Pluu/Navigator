package com.pluu.sample.featurehome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.navigator.Navigator
import com.pluu.sample.featurehome.databinding.ActivityHomeBinding
import com.pluu.sample.routeconst.Routes1
import com.pluu.utils.showToast
import com.pluu.utils.viewBinding

class HomeActivity : AppCompatActivity(R.layout.activity_home) {
    private val binding by viewBinding(ActivityHomeBinding::bind)

    private val sampleRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Provider
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

        // Graph
        binding.btnGoOnGraph.setOnClickListener {
            Navigator.of(this)
                .start(Routes1.Feature1_Graph)
        }
        binding.btnDeepLinkDefault.setOnClickListener {
            Navigator.of(this)
                .execute("pluu://feature1")
        }
        binding.btnDeepLinkSub.setOnClickListener {
            Navigator.of(this)
                .execute("pluu://feature1/1")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == sampleRequestCode) {
            showToast("Test")
        }
    }
}