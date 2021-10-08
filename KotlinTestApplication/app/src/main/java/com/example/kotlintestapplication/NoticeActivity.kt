package com.example.kotlintestapplication

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintestapplication.databinding.ActivityNoticeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NoticeActivity : AppCompatActivity() {

    private val mDatabase = FirebaseDatabase.getInstance().reference
    private val items = mutableListOf<NoticeListViewItem>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("asdasd","notice")



        mDatabase.child("Company").child(UserInfoData.getCOMP()).child("Notice").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapShot : DataSnapshot) {
                for (dataSnapShot : DataSnapshot in snapShot.children){
                    val noticeData = dataSnapShot.getValue(NoticeData::class.java)
                    items.add(NoticeListViewItem(noticeData!!.TITLE, noticeData.DATE,dataSnapShot.key.toString()))
                }
                val adapter = NoticeListViewAdapter(items)
                binding.noticeListview.adapter = adapter
            }

            override fun onCancelled(snapShot: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        binding.noticeListview.setOnItemClickListener(NoticeListener())
    }

    inner class NoticeListener : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            Log.d("code_hidding",items.get(position).noticeCode)
            val intent = Intent(this@NoticeActivity, NoticeViewActivity::class.java)
            intent.putExtra("notice_code",items.get(position).noticeCode)
            startActivity(intent)
        }
    }
}