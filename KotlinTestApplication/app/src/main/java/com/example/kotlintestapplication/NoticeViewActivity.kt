package com.example.kotlintestapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintestapplication.databinding.ActivityNoticeviewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NoticeViewActivity : AppCompatActivity() {

    private val mDatabase = FirebaseDatabase.getInstance().reference
    private lateinit var mBinding: ActivityNoticeviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNoticeviewBinding.inflate(layoutInflater)
        mBinding = binding
        setContentView(binding.root)

        val notice_code = intent.getStringExtra("notice_code")
        var title : String
        var sender : String
        var date : String
        var content : String

        mDatabase.child("Company").child(UserInfoData.getCOMP()).child("Notice").child(notice_code!!).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                setContent(binding.noticeViewTitle,binding.noticeViewSender,binding.noticeViewDate,binding.noticeViewContent,
                    p0.child("TITLE").getValue().toString(),p0.child("SENDER").getValue().toString(),
                    p0.child("DATE").getValue().toString(),p0.child("CONTENT").getValue().toString())
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    private fun setContent(getTitleID : TextView, getSenderID : TextView, getDateID : TextView, getContentID : TextView,
                           titleValue : String, senderValue : String, dateValue : String, contentValue : String){
        getTitleID.setText(titleValue)
        getSenderID.setText(senderValue)
        getDateID.setText(dateValue)
        getContentID.setText(contentValue)
    }
}