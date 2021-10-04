package com.example.kotlintestapplication

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintestapplication.databinding.ActivityConfirmBinding
import com.google.firebase.database.*

class ConfirmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmBinding
    private lateinit var mDatabase : DatabaseReference

    var item = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDatabase = FirebaseDatabase.getInstance().reference

        val myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, item)

        var listener = ListListener()
        binding.searchCompListView.setOnItemClickListener(listener)

        binding.searchCompListView.adapter = myAdapter

        binding.searchCompBt.setOnClickListener {
            if(!binding.searchCompEt.text.toString().equals("")) {
                myAdapter.clear()
                mDatabase.child("Company")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (dataSnapshot: DataSnapshot in snapshot.children) {
                                Log.d("check_key", "" + dataSnapshot.key)
                                //코틀린 null 엄격하게 지켜줘야함..................................지금까진 오히려 불편.
                                if (dataSnapshot.key?.contains(binding.searchCompEt.text.toString()) == true) {
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
            }else Toast.makeText(this, "입력해라", Toast.LENGTH_SHORT).show()
        }
    }

    inner class ListListener : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val builder = AlertDialog.Builder(this@ConfirmActivity)
            builder.setMessage(item.get(position)+"\n맞습니까??")
            builder.setPositiveButton("예") { dialog, which ->
                mDatabase.child("User").child(UserInfoData.getID()).child("COMP").setValue(item.get(position))
                UserInfoData.setCode(mDatabase.child("Company").child(UserInfoData.getCOMP()).child("CODE").key!!)
                val intent = Intent(this@ConfirmActivity, MainActivity::class.java)
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