package com.example.kotlintestapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintestapplication.databinding.ActivitySubBinding

class SubActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySubBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.subtext.setText("subsubsub")
    }
}