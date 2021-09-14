package com.example.kotlintestapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kotlintestapplication.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().reference

        binding.textview.setOnClickListener {
//            val now = System.currentTimeMillis()
//            val date = Date(now)
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
//            val getTime = dateFormat.format(date)


            val name = database.child("Company").key
            binding.textview.setText(name)
        }

        binding.button.setOnClickListener {
            val intent = Intent(this, SubActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}