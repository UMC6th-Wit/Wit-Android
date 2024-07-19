package com.umc.umc_6th_wit_android.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.databinding.FragmentHomeBinding
import com.umc.umc_6th_wit_android.databinding.FragmentSubhomeBinding

class SubHomeFragment : Fragment(){
    lateinit var binding: FragmentSubhomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubhomeBinding.inflate(inflater, container, false)

        return binding.root
    }
}