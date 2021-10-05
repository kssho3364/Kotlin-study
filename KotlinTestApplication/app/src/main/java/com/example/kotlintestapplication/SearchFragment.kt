package com.example.kotlintestapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.kotlintestapplication.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var mBinding: FragmentSearchBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentSearchBinding.inflate(inflater,container,false)
        mBinding = binding
        val complexSearchFragment = ComplexSearchFragment()
        val simpleSearchFragment = SimpleSearchFragment()

        childFragmentManager.beginTransaction().add(binding.mFrameLayout.id, simpleSearchFragment).commit()

        binding.topNaviView.run { setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_simple -> {
                    childFragmentManager.beginTransaction().replace(binding.mFrameLayout.id, simpleSearchFragment).commit()
                }
                R.id.item_complex -> {
                    childFragmentManager.beginTransaction().replace(binding.mFrameLayout.id, complexSearchFragment).commit()
                }
            }
            true
        } }
        return binding.root
    }

    override fun onResume() {
        mBinding.topNaviView.selectedItemId = R.id.item_simple
        super.onResume()
    }
}