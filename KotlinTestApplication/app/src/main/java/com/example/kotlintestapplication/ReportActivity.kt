package com.example.kotlintestapplication

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlintestapplication.databinding.ActivityReportBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*

class ReportActivity : AppCompatActivity() {
    private lateinit var dialog: Dialog
    private lateinit var imagePreview : ImageView
    private lateinit var binding : ActivityReportBinding
    private lateinit var progressDialog: ProgressDialog
    private var getCode = ArrayList<String>()

    private val mDatabase = FirebaseDatabase.getInstance().reference
    private val REQUEST_CODE = 0
    private var bitmapToString = ""
    private val random = Random()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_image_preview)
        imagePreview = dialog.findViewById<ImageView>(R.id.imagePreview)
        progressDialog = ProgressDialog(this)

        mDatabase.child("Report").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (dataSnapshot : DataSnapshot in p0.children){
                    getCode.add(dataSnapshot.key.toString())
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })

        binding.mybutton.setOnClickListener {
            val intent = Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(intent,REQUEST_CODE)

        }
        binding.isUploadTv.setOnClickListener {
            //문자열을 비트맵으로 변환
            if (!bitmapToString.equals("")){
                var baos = Base64.decode(bitmapToString,Base64.NO_WRAP)
                var bitmap = BitmapFactory.decodeByteArray(baos, 0, baos.size)
                dialog.show()
                imagePreview.setImageBitmap(bitmap)
            }

        }
        imagePreview.setOnClickListener {
            dialog.dismiss()
        }
        binding.deleteImageTv.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("삭제하시겠습니까?")
            builder.setPositiveButton("예") { dialog, which ->
                bitmapToString = ""
                binding.isUploadTv.visibility = View.GONE
                binding.deleteImageTv.visibility = View.GONE
            }
            builder.setNegativeButton("아니요", { dialog, which ->
            })
            builder.create().show()
        }
        binding.sendReportBt.setOnClickListener {
            if (!binding.reportTitleEt.text.toString().equals("")){
                if (!binding.reportContentEt.text.toString().equals("")){
                    progressDialog.show()
                    var randomkey = randomKey()
                    mDatabase.child("Report").child(randomkey).child("ID").setValue(UserInfoData.getID())
                    mDatabase.child("Report").child(randomkey).child("TITLE").setValue(binding.reportTitleEt.text.toString())
                    mDatabase.child("Report").child(randomkey).child("CONTENT").setValue(binding.reportContentEt.text.toString())
                    mDatabase.child("Report").child(randomkey).child("IMAGE").setValue(bitmapToString)
                    Log.d("bitmapString",bitmapToString)
                }else{
                    Toast.makeText(this,"내용을 입력해주세요",Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this,"제목을 입력해주세요",Toast.LENGTH_SHORT).show()
            }
            progressDialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE){
            if (resultCode == RESULT_OK){
                try {
                    //갤러리에서 가져온 이미지를 비트맵으로 변환
                    var getSome = data!!.data?.let { contentResolver.openInputStream(it) }
                    var bitmap = BitmapFactory.decodeStream(getSome)

                    getSome!!.close()

                    binding.isUploadTv.visibility = View.VISIBLE
                    binding.deleteImageTv.visibility = View.VISIBLE
                    binding.isUploadTv.text = data.data!!.lastPathSegment

                    //비트맵을 문자열로 변환하는 과정
                    var baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,baos)
                    var imageByte = baos.toByteArray()
                    bitmapToString = Base64.encodeToString(imageByte,Base64.NO_WRAP)
                    Log.d("getbitmapToString",bitmapToString)


                }catch (e : Exception){

                }
            }
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