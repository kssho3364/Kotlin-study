package com.example.kotlintestapplication

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.example.kotlintestapplication.databinding.ActivityLoginBinding
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private val REQUEST_PERMISSIONS = 2
    private val PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)

    private lateinit var binding : ActivityLoginBinding
    private lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDatabase = FirebaseDatabase.getInstance().reference


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Toast.makeText(this,"위치권한 있어야해용.. \n설정에서 위치권한 허용해주세용!", Toast.LENGTH_SHORT).show()
                finish()
            }else {
                requestPermissions(this,PERMISSIONS,REQUEST_PERMISSIONS)
            }
        }

        if (App.prefs.getValue("autoLogin","").equals("true")){
            binding.autoLogin.isChecked = true
            binding.idEt.setText(App.prefs.getValue("loginID",""))
            binding.passwordEt.setText(App.prefs.getValue("loginPW",""))
            doLogin(App.prefs.getValue("loginID",""),App.prefs.getValue("loginPW",""))
        }
        binding.loginBt.setOnClickListener {
            doLogin(binding.idEt.text.toString(),binding.passwordEt.text.toString())
//            if(!binding.idEt.text.toString().equals("") && !binding.passwordEt.text.toString().equals("")){
//                Log.d("getText",binding.idEt.text.toString())
//                var countID = 0
//                var countSnapshot = 0
//                mDatabase.child("User").addListenerForSingleValueEvent(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        for (dataSnapshot : DataSnapshot in snapshot.children){
//                            countSnapshot++
//                            //파이어베이스에서 가져온 값을 dataclass객체로 저장.
//                            val userdata = dataSnapshot.getValue(DataModel::class.java)
//                            //null을 허용해준다, 회사를 정하는건 나중이기때문에
//                            Log.d("datamodel",""+ (userdata?.COMP))
//                            if(userdata?.ID.equals(binding.idEt.text.toString())){
//                                if (userdata?.PW.equals(binding.passwordEt.text.toString())){
//
//                                    if (binding.autoLogin.isChecked==true) {
//                                        App.prefs.setValue("autoLogin","true")
//                                        App.prefs.setValue("loginID",binding.idEt.text.toString())
//                                        App.prefs.setValue("loginPW",binding.passwordEt.text.toString())
//                                    }else App.prefs.setValue("autoLogin","false")
//
//                                    if (!userdata?.COMP.equals("null")){
//
//                                        if (userdata != null) {
//                                            setUserInfo(userdata.NAME,userdata.ID,userdata.PW,userdata.COMP)
////                                            UserInfoData.setCode("a")
//                                            var intent = Intent(this@LoginActivity, MainActivity::class.java)
//                                            startActivity(intent)
//                                            finish()
//                                        }
//                                    }else{
//                                        if (userdata != null) {
//                                            setUserInfo(userdata.NAME,userdata.ID,userdata.PW,"null")
//                                            val intent = Intent(this@LoginActivity, ConfirmActivity::class.java)
//                                            startActivity(intent)
//                                            finish()
//                                        }
//                                    }
//                                }else{
//                                    Toast.makeText(this@LoginActivity,"비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show()
//                                    break
//                                }
//                            }else{
//                                countID++
//                            }
//                        }
//                        if (countID == countSnapshot){
//                            Toast.makeText(this@LoginActivity,"일치하는 아이디가 없습니다", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    override fun onCancelled(snapshot: DatabaseError) {
//
//                    }
//                })
//            }else Toast.makeText(this@LoginActivity,"입력해라",Toast.LENGTH_SHORT).show()
//
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
    private fun doLogin(getID : String, getPW: String){
        if(!getID.equals("") && !getPW.equals("")){
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
                        if(userdata?.ID.equals(getID)){
                            if (userdata?.PW.equals(getPW)){



                                if (!userdata?.COMP.equals("null")){
                                    if (userdata != null) {

                                        if (binding.autoLogin.isChecked==true) {
                                        App.prefs.setValue("autoLogin","true")
                                        App.prefs.setValue("loginID",getID)
                                        App.prefs.setValue("loginPW",getPW)
                                        }else App.prefs.setValue("autoLogin","false")

                                        setUserInfo(userdata.NAME,userdata.ID,userdata.PW,userdata.COMP)
                                        var intent = Intent(this@LoginActivity, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
                                }else{
                                    if (userdata != null) {
                                        setUserInfo(userdata.NAME,userdata.ID,userdata.PW,"null")
                                        val intent = Intent(this@LoginActivity, ConfirmActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }
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

    }
}