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
import java.util.*
import kotlin.collections.ArrayList

class ConfirmActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConfirmBinding
    private lateinit var mDatabase : DatabaseReference

    private var item = ArrayList<String>()
    private var getCode = ArrayList<String>()
    private var random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mDatabase = FirebaseDatabase.getInstance().reference

        mDatabase.child("User").child(UserInfoData.getID()).child("COMPLIST").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapshot : DataSnapshot in p0.children){
                    getCode.add(dataSnapshot.key.toString())
                    Log.d("keykeykey",""+dataSnapshot.key.toString())
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        val myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, item)

        binding.searchCompListView.setOnItemClickListener(ListListener())

        binding.searchCompListView.adapter = myAdapter

        binding.searchCompBt.setOnClickListener {
            var compname = binding.searchCompEt.text.toString().replace(" ","")
            if(!compname.equals("")) {
                myAdapter.clear()
                binding.showNothing.setText("")
                mDatabase.child("Company")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (dataSnapshot: DataSnapshot in snapshot.children) {
                                Log.d("check_key", "" + dataSnapshot.key)
                                //????????? null ???????????? ???????????????..................................???????????? ????????? ??????.
                                if (dataSnapshot.key?.contains(compname) == true) {
                                    dataSnapshot.key?.let { data -> item.add(data) }
                                }
                            }
                            if (item.isEmpty()){
                                binding.showNothing.setText("?????? ????????? ????????????.")
                                myAdapter.notifyDataSetChanged()
                            }else{
                                myAdapter.notifyDataSetChanged()
                            }
                        }

                        override fun onCancelled(snapshot: DatabaseError) {
                            //error
                        }
                    })
            }else Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show()
        }
    }

    inner class ListListener : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val builder = AlertDialog.Builder(this@ConfirmActivity)
            var key = randomKey()
            builder.setMessage(item.get(position)+"\n??????????????")
            builder.setPositiveButton("???") { dialog, which ->
                mDatabase.child("User").child(UserInfoData.getID()).child("COMP").setValue(item.get(position))
                mDatabase.child("User").child(UserInfoData.getID()).child("COMPLIST").child(key).setValue(item.get(position))
                UserInfoData.setCode(mDatabase.child("Company").child(UserInfoData.getCOMP()).child("CODE").key!!)
                Toast.makeText(this@ConfirmActivity,"?????????????????????\n?????? ?????????????????????",Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ConfirmActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("?????????", { dialog, which ->
            })
            builder.create().show()

            Log.d("test",""+item.get(position))
        }
    }
    private fun randomKey() : String{
        lateinit var key : String
        while (true){
            var key1 = String.format("%04d", random.nextInt(10000))
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