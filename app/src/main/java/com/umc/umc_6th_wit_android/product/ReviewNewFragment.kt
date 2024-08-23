package com.umc.umc_6th_wit_android.product

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.databinding.FragmentReviewNewBinding

class ReviewNewFragment : Fragment() {

    private var _binding: FragmentReviewNewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewNewBinding.inflate(inflater, container, false)
        return binding.root
    }

}