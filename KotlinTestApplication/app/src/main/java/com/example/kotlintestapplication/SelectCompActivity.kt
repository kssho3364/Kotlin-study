package com.example.kotlintestapplication

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintestapplication.databinding.ActivitySelectcompBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class SelectCompActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectcompBinding
    private val mDatabase = FirebaseDatabase.getInstance().reference
    private var item = ArrayList<String>()
    private val random = Random()
    private var getCode = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectcompBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //set ArrayList for key
        mDatabase.child("User").child(UserInfoData.getID()).child("COMPLIST").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapshot : DataSnapshot in p0.children){
                    getCode.add(dataSnapshot.key.toString())
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        val myAdapter = ArrayAdapter(this@SelectCompActivity, android.R.layout.simple_list_item_1, item)

        binding.listComp.adapter = myAdapter

        binding.listComp.setOnItemClickListener(ListListener())

        binding.searchBt.setOnClickListener {
            var compname = binding.searchCompEdit.text.toString().replace(" ","")
            if(!compname.equals("")) {
                myAdapter.clear()
                mDatabase.child("Company")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (dataSnapshot: DataSnapshot in snapshot.children) {
                                Log.d("check_key", "" + dataSnapshot.key)
                                //코틀린 null 엄격하게 지켜줘야함..................................지금까진 오히려 불편.
                                if (dataSnapshot.key?.contains(compname) == true) {
                                    dataSnapshot.key?.let { data -> item.add(data) }
                                }
                            }
                            if (item.isEmpty()){
                                binding.shownothing.setText("검색결과가 없습니다.")
                                myAdapter.notifyDataSetChanged()
                            }else{
                                myAdapter.notifyDataSetChanged()
                            }
                        }

                        override fun onCancelled(snapshot: DatabaseError) {
                            //error
                        }
                    })
            }else Toast.makeText(this@SelectCompActivity, "입력해라", Toast.LENGTH_SHORT).show()
        }
    }

    inner class ListListener : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val builder = AlertDialog.Builder(this@SelectCompActivity)
            builder.setMessage(item.get(position)+"\n맞습니까??")
            builder.setPositiveButton("예") { dialog, which ->
                //여기부터
                mDatabase.child("User").child(UserInfoData.getID()).child("COMPLIST").addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapShot : DataSnapshot) {
                        var count = 0
                        for (dataSnapShot : DataSnapshot in snapShot.children){
                            if (dataSnapShot.getValue().toString().equals(item.get(position))){
                                Toast.makeText(this@SelectCompActivity,"중복된 회사",Toast.LENGTH_SHORT).show()
                                count++
                                break
                            }
                        }
                        if (count == 0){
                            var randomkey = randomKey()
                            mDatabase.child("User").child(UserInfoData.getID()).child("COMPLIST").child(randomkey).setValue(item.get(position))
                            finish()
                        }
                    }
                    override fun onCancelled(snapShot : DatabaseError) {
                    }
                })
            }
            builder.setNegativeButton("아니요", { dialog, which ->
            })
            builder.create().show()

            Log.d("test",""+item.get(position))
        }
    }
    private fun randomKey() : String{
        lateinit var key : String
        while (true){
        var key1 = String.format("%04d",random.nextInt(10000))
            var count = 0
            for (i in 0..getCode.size-1){
                if (getCode.get(i).equals(key1)){
                    count++
                }
            }
            if (count == 0){
                key = key1
                break
            }
        }
        Log.d("key",""+key)
        return key
    }
}