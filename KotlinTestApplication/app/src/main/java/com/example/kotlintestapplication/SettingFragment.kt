package com.example.kotlintestapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlintestapplication.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSettingBinding.inflate(inflater,container,false)

        binding.showSimpleInfoTv.setText("${UserInfoData.getName()}님, 안녕하세요!")

        binding.noticeBt.setOnClickListener {
            val intent = Intent(activity,NoticeActivity::class.java)
            startActivity(intent)
        }
        binding.logoutBt.setOnClickListener {
            App.prefs.setValue("autoLogin","")
            App.prefs.setValue("loginID","")
            App.prefs.setValue("loginPW","")

            val intent = Intent(activity,LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        binding.mypageBt.setOnClickListener {
            val intent = Intent(activity, MyPageActivity::class.java)
            startActivity(intent)
        }
        binding.settingcompBt.setOnClickListener {
            val intent = Intent(activity, SettingCompActivity::class.java)
            startActivity(intent)
        }
        binding.settingBt.setOnClickListener {
            val intent = Intent(activity, SettingActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}