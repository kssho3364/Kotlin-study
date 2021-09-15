package com.example.kotlintestapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintestapplication.databinding.ActivityLoginBinding
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDatabase = FirebaseDatabase.getInstance().reference

        binding.loginBt.setOnClickListener {
            if(!binding.idEt.text.toString().equals("") && !binding.passwordEt.text.toString().equals("")){
                Log.d("getText",binding.idEt.text.toString())
                mDatabase.child("User").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        for (dataSnapshot : DataSnapshot in p0.children){
                            Log.d("asd","aa\n"+dataSnapshot.toString())
                        }
                    }
                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
            }else Toast.makeText(this@LoginActivity,"입력해라",Toast.LENGTH_SHORT).show()

//            var intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }
        binding.registerBt.setOnClickListener {
            var intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }


    }
}