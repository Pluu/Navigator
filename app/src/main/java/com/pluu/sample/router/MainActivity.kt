package com.pluu.sample.router

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.pluu.navigator.Navigator
import com.pluu.sample.routeconst.Home
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenStarted {
            delay(1_000)
            Navigator.of(this@MainActivity)
                .start(Home.Default)
            finish()
        }
    }
}