package com.example.kotlintestapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class NoticeListViewAdapter(private val items : MutableList<NoticeListViewItem>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): NoticeListViewItem = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var convertView = view
        if(convertView == null) convertView = LayoutInflater.from(parent?.context).inflate(R.layout.listview_notice,parent,false)

        val item : NoticeListViewItem = items[position]
        convertView!!.findViewById<TextView>(R.id.notice_title_tv).text = item.Title
        convertView!!.findViewById<TextView>(R.id.notice_date_tv).text = item.Date
        convertView!!.findViewById<TextView>(R.id.noticeCode).text = item.noticeCode

        return convertView
    }
}