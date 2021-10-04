package com.example.kotlintestapplication

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class MySharedPrefs(context: Context) {
    val prefs: SharedPreferences = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getValue(key : String, value : String) : String{
        return prefs.getString(key, value).toString()
    }
    fun setValue(key : String, value : String){
        prefs.edit().putString(key,value).apply()
    }
}