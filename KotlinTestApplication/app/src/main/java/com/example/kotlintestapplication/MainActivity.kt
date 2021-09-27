package com.example.kotlintestapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintestapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("application",""+UserInfoData.getName()+UserInfoData.getCOMP())
    }
}