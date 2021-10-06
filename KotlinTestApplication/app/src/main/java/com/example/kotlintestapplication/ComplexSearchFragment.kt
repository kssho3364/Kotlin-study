package com.example.kotlintestapplication

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kotlintestapplication.databinding.FragmentComplexSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat

class ComplexSearchFragment : Fragment() {

    private lateinit var selectDateDialog: Dialog
    private lateinit var mBinding: FragmentComplexSearchBinding
    private lateinit var selectYear : NumberPicker
    private lateinit var selectMonth : NumberPicker
    private lateinit var selectDay : NumberPicker
    private lateinit var dialogCheck_bt : Button
    private lateinit var dialogCancel_bt : Button
    private val mDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentComplexSearchBinding.inflate(inflater,container,false)
        mBinding = binding

        selectDateDialog = Dialog(requireContext())
        selectDateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        selectDateDialog.setContentView(R.layout.select_date_dialog)
        selectYear = selectDateDialog.findViewById(R.id.select_year_np)
        selectMonth = selectDateDialog.findViewById(R.id.select_month_np)
        selectDay = selectDateDialog.findViewById(R.id.select_date_np)
        dialogCheck_bt = selectDateDialog.findViewById(R.id.dialog_check_bt)
        dialogCancel_bt = selectDateDialog.findViewById(R.id.dialog_cancel_bt)

        val items = mutableListOf<SearchListViewItem>()

        setDate()

        binding.searchDetail.setOnClickListener {
//            items.add(SearchListViewItem("","","")) 기본 형태.
            items.clear()

            val startDate = binding.startDate.text.toString().replace("-","").toInt()
            val finishDate = binding.finishDate.text.toString().replace("-","").toInt()

            mDatabase.child("Company").child(UserInfoData.getCOMP()).child("Attend").child(UserInfoData.getID()).addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onDataChange(p0: DataSnapshot) {

                    for (dataSnapshot : DataSnapshot in p0.children){

                        val baseDate = dataSnapshot.key!!.replace("-","").toInt()

                        if (baseDate >= startDate && baseDate <= finishDate){

                            var startDay = p0.child(dataSnapshot.key.toString()).child("출근").value.toString()
                            var finishDay = p0.child(dataSnapshot.key.toString()).child("퇴근").value.toString()

                            if (startDay.equals("null")){
                                startDay = "정보없음"
                            }

                            if (finishDay.equals("null")){
                                finishDay = "정보없음"
                            }
                            items.add(SearchListViewItem(dataSnapshot.key.toString(),startDay,finishDay))
                        }
                    }

                    val adapter = SearchListViewAdapter(items)
                    binding.searchDetailListView.adapter = adapter
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }

        binding.startDateLayout.setOnClickListener {
            showDialog("start")
        }

        binding.finishDateLayout.setOnClickListener {
            showDialog("finish")
        }

        return binding.root
    }
    private fun showDialog(kind : String){

        lateinit var guessWhat : TextView

        if (kind.equals("start")) guessWhat = mBinding.startDate
        else guessWhat = mBinding.finishDate

        setValue(selectYear,2030,2010)
        setValue(selectMonth, 12, 1)
        setValue(selectDay, 31, 1)

            val splitDate = guessWhat.text.toString().split("-")
            selectYear.value = splitDate[0].toInt()
            selectMonth.value = splitDate[1].toInt()
            selectDay.value = splitDate[2].toInt()

        dialogCheck_bt.setOnClickListener {
            guessWhat.setText(String.format("%d-%02d-%02d",selectYear.value, selectMonth.value, selectDay.value))
            selectDateDialog.dismiss()
        }
        dialogCancel_bt.setOnClickListener {
            selectDateDialog.dismiss()
        }

        selectDateDialog.show()
    }
    private fun setValue(selectAny : NumberPicker, max : Int, min : Int){
        selectAny.maxValue = max
        selectAny.minValue = min
    }
    private fun setDate(){
        val simpleFormat = SimpleDateFormat("yyyy-MM-dd")
        mBinding.startDate.setText(simpleFormat.format(System.currentTimeMillis()))
        mBinding.finishDate.setText(simpleFormat.format(System.currentTimeMillis()))
    }
}