package com.umc.umc_6th_wit_android.home.ranking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentHomeBinding
import com.umc.umc_6th_wit_android.databinding.FragmentPeriodBinding
import com.umc.umc_6th_wit_android.home.HomeVPAdapter

class PeriodFragment : Fragment() {

    lateinit var binding: FragmentPeriodBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPeriodBinding.inflate(inflater, container, false)

        return binding.root
    }


}