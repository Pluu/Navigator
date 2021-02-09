package com.pluu.sample.router

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.navigator.Navigator
import com.pluu.sample.routeconst.Home

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Navigator.of(this@MainActivity)
            .start(Home.Default)
        finish()
    }
}