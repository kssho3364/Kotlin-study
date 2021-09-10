package com.example.kotlintestapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlintestapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var mBinding : ActivityMainBinding? =null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textview.setOnClickListener {
            binding.textview.setText("Hello, Kotlin")
        }
    }
}