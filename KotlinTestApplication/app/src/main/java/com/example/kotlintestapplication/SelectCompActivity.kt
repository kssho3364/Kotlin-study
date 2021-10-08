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

class SelectCompActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectcompBinding
    private val mDatabase = FirebaseDatabase.getInstance().reference
    private var item = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectcompBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myAdapter = ArrayAdapter(this@SelectCompActivity, android.R.layout.simple_list_item_1, item)

        binding.listComp.adapter = myAdapter

//        binding.listComp.setOnItemClickListener(ListListener())

        binding.searchBt.setOnClickListener {
            if(!binding.searchCompEdit.text.toString().equals("")) {
                myAdapter.clear()
                mDatabase.child("Company")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (dataSnapshot: DataSnapshot in snapshot.children) {
                                Log.d("check_key", "" + dataSnapshot.key)
                                //코틀린 null 엄격하게 지켜줘야함..................................지금까진 오히려 불편.
                                if (dataSnapshot.key?.contains(binding.searchCompEdit.text.toString()) == true) {
                                    dataSnapshot.key?.let { data -> item.add(data) }
                                }
                            }
                            if (item.isEmpty()){
                                item.add("검색 결과가 없습니다.")
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
               mDatabase.child("User").child(UserInfoData.getID()).child("COMP").setValue(item.get(position))
                UserInfoData.setCode(mDatabase.child("Company").child(UserInfoData.getCOMP()).child("CODE").key!!)
                Toast.makeText(this@SelectCompActivity,"등록되었습니다\n다시 로그인해주세요",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@SelectCompActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("아니요", { dialog, which ->
            })
            builder.create().show()

            Log.d("test",""+item.get(position))
        }
    }
}