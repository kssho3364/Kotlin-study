package com.example.kotlintestapplication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintestapplication.databinding.ActivityMypageBinding
import com.google.firebase.database.FirebaseDatabase

class MyPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMypageBinding
    private lateinit var modiDialod : Dialog
    private lateinit var passwordAccept_bt : TextView
    private lateinit var passwordCancel_bt: TextView
    private lateinit var inputPassword : EditText
    private val mDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modiDialod = Dialog(this@MyPageActivity)
        modiDialod.requestWindowFeature(Window.FEATURE_NO_TITLE)
        modiDialod.setContentView(R.layout.dialog_modifyinfo)

        passwordAccept_bt = modiDialod.findViewById(R.id.password_accept)
        passwordCancel_bt = modiDialod.findViewById(R.id.password_cancel)
        inputPassword = modiDialod.findViewById(R.id.inputPassword_edit)

        binding.modifyInfoBt.setOnClickListener {
            showModiDialog()
        }
    }
    private fun showModiDialog(){
        modiDialod.show()

        passwordAccept_bt.setOnClickListener {
            if (inputPassword.text.toString().equals(UserInfoData.getPW())){
                val intent = Intent(this,ModifyInfoActivity::class.java)
                startActivity(intent)
                modiDialod.dismiss()
            }else{
                Toast.makeText(this,"비밀번호가 다릅니다.",Toast.LENGTH_SHORT).show()
            }
        }
        passwordCancel_bt.setOnClickListener {
            modiDialod.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.showInfoNameTv.setText(UserInfoData.getName())
        binding.showInfoIDTv.setText(UserInfoData.getID())
        binding.showInfoCompTv.setText(UserInfoData.getCOMP())
    }
}