package com.example.kotlintestapplication

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.*
//import android.hardware.biometrics.BiometricPrompt
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kotlintestapplication.databinding.FragmentAttendBinding
import java.util.concurrent.Executor
import androidx.biometric.BiometricPrompt
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*


class AttendFragment : Fragment() {

    private var attendCode = ""
    private val REQEST_ENABLE_LOCATION = 100
    private val REQUEST_ENABLE_BT=1
    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
    private val mDatabase : DatabaseReference = FirebaseDatabase.getInstance().reference

    private lateinit var executor : Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var locationManager: LocationManager



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentAttendBinding.inflate(inflater,container,false)

        getCompanyCode()

        //얘를 밖에서 선언하면 오류뜸 왜지 ㄹㅇ??
        val locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        binding.attendBt.setOnClickListener {
            setLocationManager()
            setBluetooth()
            if (bluetoothAdapter.isEnabled && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                attendCode = "출근"
                doBiometric()
            }
        }

        binding.workoffBt.setOnClickListener {
            setLocationManager()
            setBluetooth()
            if (bluetoothAdapter.isEnabled && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                attendCode = "퇴근"
                doBiometric()
            }
        }

        return binding.root
    }
    fun setBluetooth(){
        if (!bluetoothAdapter.isEnabled){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    private val onReceive = object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val action : String? = intent?.action
            when(action){
                BluetoothDevice.ACTION_FOUND -> {
                    val device : BluetoothDevice? = intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    Log.d("found device", ""+UserInfoData.getCode())
                    if (deviceName.equals(UserInfoData.getCode())){
                        doAttend()
                    }
                }
            }
        }
    }

    private fun doBiometric(){

        executor = ContextCompat.getMainExecutor(requireContext())

        biometricPrompt = BiometricPrompt(requireActivity(),executor, object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(requireContext(),"error",Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                startBluetoothScan()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(requireContext(),"유효한 인식이 필요합니다.",Toast.LENGTH_SHORT).show()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("생체인증")
//            .setSubtitle("생체정보로 인증해 주세요")
            .setNegativeButtonText("취소")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    private fun setLocationManager() {
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("위치 서비스를 켜시겠습니까?")
            builder.setPositiveButton("예", DialogInterface.OnClickListener { dialog, which ->
                val locationIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(locationIntent, REQEST_ENABLE_LOCATION)
            })
            builder.setNegativeButton("아니요", DialogInterface.OnClickListener { dialog, which ->
                Toast.makeText(requireContext(), "위치 서비스를 켜야합니다.", Toast.LENGTH_SHORT).show()
            })
            builder.create().show()
        }
    }

    private fun startBluetoothScan(){
        requireActivity().registerReceiver(onReceive, filter)
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        bluetoothAdapter.startDiscovery()
    }

    private fun doAttend(){
        val today = System.currentTimeMillis()
        val date = Date(today)
        val mDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val mTimeFormat = SimpleDateFormat("HH:mm:ss")
        val dayNow = mDateFormat.format(date)
        val timeNow = mTimeFormat.format(date)
        mDatabase.child("Company").child(UserInfoData.getCOMP()).child("Attend").child(UserInfoData.getID()).child(dayNow).child(attendCode).setValue(timeNow)
        Toast.makeText(requireContext(),""+attendCode+" 되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun getCompanyCode(){
        mDatabase.child("Company").child(UserInfoData.getCOMP()).child("CODE").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                UserInfoData.setCode(p0.value.toString())
            }
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(requireContext(),"주소를 불러오지 못했습니다.",Toast.LENGTH_SHORT).show()
            }
        })
    }
}