package com.umc.umc_6th_wit_android.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentTemplateCategoryBinding


class TemplateCategoryFragment : Fragment() {
    lateinit var binding: FragmentTemplateCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTemplateCategoryBinding.inflate(inflater, container, false)

        return binding.root
    }
}