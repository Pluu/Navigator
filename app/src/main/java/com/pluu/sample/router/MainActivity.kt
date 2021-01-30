package com.pluu.sample.router

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.navigator.Navigator
import com.pluu.sample.routeconst.Routes1
import com.pluu.sample.router.databinding.ActivityMainBinding
import com.pluu.utils.viewBinding

class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding.btnGo.setOnClickListener {
            Navigator.of(this).start(Routes1.Feature1)
        }
    }
}