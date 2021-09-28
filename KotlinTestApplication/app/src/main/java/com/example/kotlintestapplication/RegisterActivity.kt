package com.example.kotlintestapplication

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintestapplication.databinding.ActivityRegisterBinding
import com.google.firebase.database.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDatabase = FirebaseDatabase.getInstance().reference

        Log.d("application",""+UserInfoData.getName()+UserInfoData.getCOMP())

        binding.duplicteCheckBt.setOnClickListener {
            var idCount = 0
            mDatabase.child("User").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot : DataSnapshot) {
                    for (dataSnapshot : DataSnapshot in snapshot.children){
                        val userData = dataSnapshot.getValue(DataModel::class.java)
                        if (userData?.ID.equals(binding.regitIDEt.text.toString())){
                            idCount++
                        }
                    }
                    if (idCount == 0){
                        val builder = AlertDialog.Builder(this@RegisterActivity)
                        builder.setMessage("사용가능한 아이디입니다.\n사용하시겠습니까?")
                        builder.setPositiveButton("예",DialogInterface.OnClickListener { dialog, which ->
                            binding.regitIDEt.focusable = View.NOT_FOCUSABLE
                            binding.joinBt.visibility = View.VISIBLE
                        })
                        builder.setNegativeButton("아니요", DialogInterface.OnClickListener { dialog, which ->
                        })
                        builder.create().show()
                    }else Toast.makeText(this@RegisterActivity, "사용할 수 없는 아이디입니다.", Toast.LENGTH_SHORT).show()
                }
                override fun onCancelled(snapshot : DatabaseError) {
                        Toast.makeText(this@RegisterActivity, "서버에 연결할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.joinBt.setOnClickListener {
            if (binding.regitPWEt.text.toString().equals(binding.regitPWCheckEt.text.toString())){
                val builder = AlertDialog.Builder(this@RegisterActivity)
                builder.setMessage("회원가입 하시겠습니까?")
                builder.setPositiveButton("예",DialogInterface.OnClickListener { dialog, which ->
                    creatAccount(binding.regitIDEt.text.toString(), binding.regitPWEt.text.toString(), binding.regitNameEt.text.toString())
                })
                builder.setNegativeButton("아니요", DialogInterface.OnClickListener { dialog, which ->
                })
                builder.create().show()
                creatAccount(binding.regitIDEt.text.toString(), binding.regitPWEt.text.toString(), binding.regitNameEt.text.toString())
                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(this@RegisterActivity,"회원가입이 완료되었습니다.",Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this@RegisterActivity, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun creatAccount(ID:String, PW:String, NAME:String){
        mDatabase.child("User").child(ID).child("ID").setValue(ID)
        mDatabase.child("User").child(ID).child("PW").setValue(PW)
        mDatabase.child("User").child(ID).child("NAME").setValue(NAME)
        mDatabase.child("User").child(ID).child("COMP").setValue("null")

    }
}