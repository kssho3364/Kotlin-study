package com.example.kotlintestapplication

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlintestapplication.databinding.FragmentAttendBinding

class AttendFragment : Fragment() {

    private val REQUEST_ENABLE_BT=1

    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentAttendBinding.inflate(inflater,container,false)


        binding.attendBt.setOnClickListener {
            setBluetooth()
        }



        return binding.root
    }
    fun setBluetooth(){
        if(bluetoothAdapter.isEnabled){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }
}