package com.example.kotlintestapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView

class SettingCompListViewAdapter(private val items : MutableList<SettingCompListViewItem>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): SettingCompListViewItem = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var convertView = view
        if(convertView == null) convertView = LayoutInflater.from(parent?.context).inflate(R.layout.listview_setting_comp,parent,false)

        val item : SettingCompListViewItem = items[position]
        convertView!!.findViewById<TextView>(R.id.compNameList).text = item.CompName
        convertView!!.findViewById<CheckBox>(R.id.compCheckBox).isChecked = item.CompCheck
        convertView!!.findViewById<CheckBox>(R.id.compCheckBox).visibility = item.CheckBoxVisible
        convertView!!.findViewById<TextView>(R.id.code_hiding).text = item.codehiding
//        Log.d("position",""+position)
        Log.d("지나가나","ㅇㅇ")
        return convertView
    }
}