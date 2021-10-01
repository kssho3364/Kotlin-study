package com.example.kotlintestapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.kotlintestapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val attendFragment = AttendFragment()
        val searchFragment = SearchFragment()
        val settingFragment = SettingFragment()

        supportFragmentManager.beginTransaction().add(R.id.fargment_Layout, attendFragment).commit()

        binding.bottomNavigationView.run { setOnItemSelectedListener {
                when(it.itemId){
                    R.id.item_attendFrag -> {
                        supportFragmentManager.beginTransaction().replace(binding.fargmentLayout.id, attendFragment).commit()
                    }
                    R.id.item_searchFrag -> {
                        supportFragmentManager.beginTransaction().replace(binding.fargmentLayout.id, searchFragment).commit()
                    }
                    R.id.item_settingFrag -> {
                        supportFragmentManager.beginTransaction().replace(binding.fargmentLayout.id, settingFragment).commit()
                    }
                }
                true
        } }
    }
}