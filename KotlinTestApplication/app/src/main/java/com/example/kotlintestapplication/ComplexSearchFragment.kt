package com.example.kotlintestapplication

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kotlintestapplication.databinding.FragmentComplexSearchBinding

class ComplexSearchFragment : Fragment() {
    private lateinit var selectDateDialog: Dialog
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentComplexSearchBinding.inflate(inflater,container,false)

        selectDateDialog = Dialog(requireContext())
        selectDateDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        selectDateDialog.setContentView(R.layout.select_date_dialog)

        binding.finishDateLayout.setOnClickListener {
            selectDateDialog.show()
        }



        return binding.root
    }
}