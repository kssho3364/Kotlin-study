package com.example.kotlintestapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintestapplication.databinding.ActivitySelectcompBinding

class SelectCompActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectcompBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectcompBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}