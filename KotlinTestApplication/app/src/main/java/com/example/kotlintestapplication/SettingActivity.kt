package com.example.kotlintestapplication

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintestapplication.databinding.ActivitySettingBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SettingActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding
    private val mDatabase = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDatabase.child("version").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                binding.AppVersion.setText("앱버전 : "+p0.getValue().toString())
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        binding.reportBt.setOnClickListener {
            val intent = Intent(this,ReportActivity::class.java)
            startActivity(intent)
        }
        binding.withDrawBt.setOnClickListener {
            var builder = AlertDialog.Builder(this@SettingActivity)
            builder.setMessage("정말 탈퇴하시겠습니까?")
            builder.setPositiveButton("예"){dialog, which ->
                mDatabase.child("User").child(UserInfoData.getID()).removeValue()
                val intent = Intent(this,LoginActivity::class.java)
                UserInfoData.setID("")
                UserInfoData.setCode("")
                UserInfoData.setPW("")
                UserInfoData.setName("")
                UserInfoData.setCOMP("")
                App.prefs.setValue("loginID","")
                App.prefs.setValue("loginPW","")
                App.prefs.setValue("autoLogin","false")
                App.prefs.setValue("attend_status_now","")
                App.prefs.setValue("attend_status_time","")
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("아니오"){dialog, which ->

            }
            builder.show()
        }
    }

    override fun onResume() {
        super.onResume()
        mDatabase.child("version").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                binding.AppVersion.setText("앱버전 : "+p0.getValue().toString())
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}