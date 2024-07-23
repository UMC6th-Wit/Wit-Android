package com.umc.umc_6th_wit_android.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.umc_6th_wit_android.data.local.CategoryDao
import com.umc.umc_6th_wit_android.data.local.PersonalDao
import com.umc.umc_6th_wit_android.databinding.FragmentTemplateCategoryBinding

class CategoryFragment : Fragment() {
    lateinit var binding: FragmentTemplateCategoryBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTemplateCategoryBinding.inflate(inflater, container, false)

        val items = CategoryDao().items

        val adapter = CategoryRVAdapter(items)
        binding.categoryRv.adapter = adapter
        binding.categoryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        return binding.root
    }
}