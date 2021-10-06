package com.example.kotlintestapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class SearchListViewAdapter(private val items : MutableList<SearchListViewItem>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): SearchListViewItem = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var convertView = view
        if(convertView == null) convertView = LayoutInflater.from(parent?.context).inflate(R.layout.listview_search,parent,false)

        val item : SearchListViewItem = items[position]
        convertView!!.findViewById<TextView>(R.id.listview_day_tv).text = item.day
        convertView!!.findViewById<TextView>(R.id.listview_attend_tv).text = item.attendTime
        convertView!!.findViewById<TextView>(R.id.listview_offwork_tv).text = item.offWorkTime

        return convertView
    }
}