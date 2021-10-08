package com.example.kotlintestapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintestapplication.databinding.ActivitySettingcompBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SettingCompActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingcompBinding
    private lateinit var adapter: SettingCompListViewAdapter

    private val mDatabase = FirebaseDatabase.getInstance().reference
    private val items = mutableListOf<SettingCompListViewItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingcompBinding.inflate(layoutInflater)
        setContentView(binding.root)

        SetListView(View.GONE,"complete")

        binding.deleteCompBt.setOnClickListener {
            SetListView(View.VISIBLE,"delete")
            binding.scanCompListview.setOnItemClickListener(CompListListener())
        }

        binding.addCompBt.setOnClickListener {
            val intent = Intent(this@SettingCompActivity,SelectCompActivity::class.java)
            startActivity(intent)
        }

        binding.CompleteBt.setOnClickListener {
            SetListView(View.GONE,"complete")
            binding.scanCompListview.setOnItemClickListener(ListenerEnable())
        }
    }

    private fun SetListView(visibility : Int, kind : String){
        items.clear()
        mDatabase.child("User").child(UserInfoData.getID()).child("COMPLIST").addListenerForSingleValueEvent(object : ValueEventListener{
            var count = 0
            override fun onDataChange(snapShot: DataSnapshot) {
                for (dataSnapShot : DataSnapshot in snapShot.children){
                    count++
                    Log.d("value", snapShot.child(count.toString()).getValue().toString())
                    items.add(SettingCompListViewItem(snapShot.child(count.toString()).getValue().toString(),false,visibility))
                }
                notificationChage()
                if (kind.equals("delete")){
                    setButton(View.GONE,View.GONE,View.VISIBLE)
                }else{
                    setButton(View.VISIBLE,View.VISIBLE,View.GONE)
                }
            }
            override fun onCancelled(snapShot: DatabaseError) {
            }
        })
    }

    inner class CompListListener : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            Log.d("타긴하나?","탔다")

            val item = parent?.getItemAtPosition(position) as SettingCompListViewItem
            if (item.CompCheck==true){
                items.set(position,SettingCompListViewItem(item.CompName,false,View.VISIBLE))

            }else items.set(position,SettingCompListViewItem(item.CompName,true,View.VISIBLE))

            adapter = SettingCompListViewAdapter(items)
            binding.scanCompListview.adapter = adapter
            Log.d("getPosotion",""+item.CompName)
        }
    }
    inner class ListenerEnable : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        }
    }
    private fun notificationChage(){
        adapter = SettingCompListViewAdapter(items)
        binding.scanCompListview.adapter = adapter
    }
    private fun setButton(delete : Int, add : Int, complete : Int){
        binding.CompleteBt.visibility = complete
        binding.deleteCompBt.visibility = delete
        binding.addCompBt.visibility = add
    }
}