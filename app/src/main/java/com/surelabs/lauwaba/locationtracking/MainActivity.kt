package com.surelabs.lauwaba.locationtracking

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationStart.setOnClickListener {
            startService(Intent(this@MainActivity, MyLocationServices::class.java))
        }

        locationStop.setOnClickListener {
            stopService(Intent(this@MainActivity, MyLocationServices::class.java))
        }
    }
}