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

        binding.testBt.setOnClickListener {
            UserInfoData.setName("김상호")
            UserInfoData.setCOMP("광교종합사회복지관")
        }

        binding.loginBt.setOnClickListener {
            if(!binding.idEt.text.toString().equals("") && !binding.passwordEt.text.toString().equals("")){
                Log.d("getText",binding.idEt.text.toString())
                var countID = 0
                var countSnapshot = 0
                mDatabase.child("User").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dataSnapshot : DataSnapshot in snapshot.children){
                            countSnapshot++
                            //파이어베이스에서 가져온 값을 dataclass객체로 저장.
                            val userdata = dataSnapshot.getValue(DataModel::class.java)
                            //null을 허용해준다, 회사를 정하는건 나중이기때문에
                            Log.d("datamodel",""+ (userdata?.COMP))
                            if(userdata?.ID.equals(binding.idEt.text.toString())){
                                if (userdata?.PW.equals(binding.passwordEt.text.toString())){
                                    if (!userdata?.COMP.equals("null")){
                                        if (userdata != null) {
                                            setUserInfo(userdata.NAME,userdata.ID,userdata.PW,userdata.COMP)
                                            var intent = Intent(this@LoginActivity, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                    }else{
                                            Toast.makeText(this@LoginActivity, "신규회원",Toast.LENGTH_SHORT).show()
                                        break
                                    }
                                }else{
                                    Toast.makeText(this@LoginActivity,"비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
                                    break
                                }
                            }else{
                                countID++
                            }
                        }
                        if (countID == countSnapshot){
                            Toast.makeText(this@LoginActivity,"일치하는 아이디가 없습니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(snapshot: DatabaseError) {

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
    private fun setUserInfo(NAME:String, ID:String, PW:String, COMP:String){
        UserInfoData.setName(NAME)
        UserInfoData.setPW(PW)
        UserInfoData.setID(ID)
        UserInfoData.setCOMP(COMP)
    }
    private fun doLogin(){

    }
}