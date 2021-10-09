package com.example.kotlintestapplication

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
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
    private lateinit var changeCompDialog: Dialog
    private lateinit var changeCompListView : ListView

    private val mDatabase = FirebaseDatabase.getInstance().reference
    private val items = mutableListOf<SettingCompListViewItem>()
    private var changeCompItem = ArrayList<String>()
    private val selectedItem = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingcompBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeCompDialog = Dialog(this@SettingCompActivity)
        changeCompDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        changeCompDialog.setContentView(R.layout.dialog_changecomp)

        changeCompListView = changeCompDialog.findViewById(R.id.changeCompListView)

        binding.changeCompBt.setOnClickListener {
            showChangeCompDialog()
        }

        binding.deleteCompBt.setOnClickListener {
            SetListView(View.VISIBLE,"delete")
            binding.scanCompListview.setOnItemClickListener(CompListListener())
        }

        binding.addCompBt.setOnClickListener {
            val intent = Intent(this@SettingCompActivity,SelectCompActivity::class.java)
            startActivity(intent)
        }

        binding.CompleteBt.setOnClickListener {
            for (i in 0..selectedItem.size-1){
                Log.d("list",""+selectedItem.get(i))
                mDatabase.child("User").child(UserInfoData.getID()).child("COMPLIST").child(selectedItem.get(i)).removeValue()
            }
            SetListView(View.GONE,"complete")
            binding.scanCompListview.setOnItemClickListener(ListenerEnable())
            selectedItem.clear()
        }
    }

    private fun SetListView(visibility : Int, kind : String){
        items.clear()
        changeCompItem.clear()
        mDatabase.child("User").child(UserInfoData.getID()).child("COMPLIST").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapShot: DataSnapshot) {
                for (dataSnapShot : DataSnapshot in snapShot.children){
                    items.add(SettingCompListViewItem(snapShot.child(dataSnapShot.key.toString()).getValue().toString(),false,visibility,dataSnapShot.key.toString()))
                    changeCompItem.add(snapShot.child(dataSnapShot.key.toString()).getValue().toString())
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
                items.set(position,SettingCompListViewItem(item.CompName,false,View.VISIBLE,item.codehiding))
                selectedItem.remove(item.codehiding)

            }else {
                items.set(position,SettingCompListViewItem(item.CompName,true,View.VISIBLE,item.codehiding))
                selectedItem.add(item.codehiding)
            }

            adapter = SettingCompListViewAdapter(items)
            binding.scanCompListview.adapter = adapter
            Log.d("getPosotion",""+item.codehiding)

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

    private fun showChangeCompDialog(){
        changeCompDialog.show()

        val changeCompAdapter = ArrayAdapter(changeCompDialog.context,android.R.layout.simple_list_item_1,changeCompItem)
        changeCompListView.adapter = changeCompAdapter
        changeCompAdapter.notifyDataSetChanged()

        changeCompListView.setOnItemClickListener(changeListListener())
    }
    inner class changeListListener : AdapterView.OnItemClickListener{
        override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val builder = AlertDialog.Builder(this@SettingCompActivity)
            builder.setMessage(changeCompItem.get(position)+"\n맞습니까??")
            builder.setPositiveButton("예") { dialog, which ->
                mDatabase.child("User").child(UserInfoData.getID()).child("COMP").setValue(changeCompItem.get(position))
                UserInfoData.setCOMP(changeCompItem.get(position))

                binding.changeCompNameTv.setText(changeCompItem.get(position))

                changeCompDialog.dismiss()
            }
            builder.setNegativeButton("아니요", { dialog, which ->
                changeCompDialog.dismiss()
            })
            builder.create().show()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("onResume","in")
        SetListView(View.GONE,"complete")
        binding.changeCompNameTv.setText(UserInfoData.getCOMP())
    }
}