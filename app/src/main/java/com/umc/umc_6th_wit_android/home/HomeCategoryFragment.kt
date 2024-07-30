package com.umc.umc_6th_wit_android.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.umc_6th_wit_android.data.local.CategoryDao
import com.umc.umc_6th_wit_android.databinding.FragmentHomeCategoryBinding


class HomeCategoryFragment : Fragment() {
    lateinit var binding: FragmentHomeCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeCategoryBinding.inflate(inflater, container, false)

        val items = CategoryDao().items

        val adapter = CategoryRVAdapter(items)
        binding.categoryRv.adapter = adapter
        binding.categoryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        return binding.root
    }
}