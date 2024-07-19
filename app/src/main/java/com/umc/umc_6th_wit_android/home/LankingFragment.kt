package com.umc.umc_6th_wit_android.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentHomeBinding
import com.umc.umc_6th_wit_android.databinding.FragmentLankingBinding

class LankingFragment : Fragment() {

    lateinit var binding: FragmentLankingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLankingBinding.inflate(inflater, container, false)

        return binding.root
    }

}