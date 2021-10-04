package com.example.kotlintestapplication

import android.app.Application
import android.content.SharedPreferences

class App : Application() {
    companion object{
        lateinit var prefs: MySharedPrefs
    }

    override fun onCreate() {
        prefs = MySharedPrefs(applicationContext)
        super.onCreate()
    }
}