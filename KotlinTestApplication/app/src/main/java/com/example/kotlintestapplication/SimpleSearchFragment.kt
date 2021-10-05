package com.example.kotlintestapplication

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kotlintestapplication.databinding.FragmentSimpleSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SimpleSearchFragment : Fragment() {

    private val mDatabase = FirebaseDatabase.getInstance().reference

    private lateinit var mBinding : FragmentSimpleSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSimpleSearchBinding.inflate(inflater,container,false)
        mBinding = binding

        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val date = String.format("%d-%02d-%02d",year,month+1,dayOfMonth)
            Log.d("date",date)
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage(""+year+"년"+month+"월"+dayOfMonth+"일"+"\n검색하시겠습니까?")
            builder.setPositiveButton("예"){ dialog, which ->
                getAttendInfo(date)
            }
            builder.setNegativeButton("아니오"){dialog, which ->
            }
            builder.create().show()
        }

        return binding.root
    }

    private fun getAttendInfo(date : String) {
        Log.d("getInfo", "" + UserInfoData.getCOMP() + ", " + UserInfoData.getID() + ", " + date)
        val array = arrayOf("출근", "퇴근")
        for (i in 0..1) {
            mDatabase.child("Company").child(UserInfoData.getCOMP()).child("Attend")
                .child(UserInfoData.getID()).child(date).child(array[i])
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        if (array[i].equals("출근")){
                            if (p0.value.toString().equals("null")){
                                mBinding.showAttendTime.setText("정보 없음")
                            }else{
                                mBinding.showAttendTime.setText(p0.value.toString())
                            }
                        }
                        else {
                            if(p0.value.toString().equals("null")){
                                mBinding.showOffworkTime.setText("정보 없음")
                            } else{
                                mBinding.showOffworkTime.setText(p0.value.toString())
                            }
                        }
                    }
                    override fun onCancelled(p0: DatabaseError) {
                        Toast.makeText(requireContext(), "서버에 연결할 수 없습니다", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
        }
    }
}
