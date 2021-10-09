package com.example.kotlintestapplication

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintestapplication.databinding.ActivityModifyinfoBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ModifyInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityModifyinfoBinding
    private val mDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityModifyinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.modiComplete.setOnClickListener {
            if (!binding.inputNameModi.text.toString().equals("") && !binding.inputPWModi.text.toString().equals("")) {
                if (binding.inputPWModi.text.toString()
                        .equals(binding.inputPWModiRe.text.toString())
                ) {
                    mDatabase.child("User").child(UserInfoData.getID()).child("PW").setValue(binding.inputPWModi.text.toString())
                    mDatabase.child("User").child(UserInfoData.getID()).child("NAME").setValue(binding.inputNameModi.text.toString())
                    UserInfoData.setName(binding.inputNameModi.text.toString())
                    UserInfoData.setPW(binding.inputPWModi.text.toString())

                    Toast.makeText(this,"수정되었습니다.",Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this,"비밀번호 확인해주삼!",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"입력하삼!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}