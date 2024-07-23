package com.umc.umc_6th_wit_android.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.databinding.FragmentSerchBinding

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSerchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSerchBinding.inflate(inflater, container, false)
        return binding.root
    }
}