package com.umc.umc_6th_wit_android.home.ranking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.local.CategoryDao
import com.umc.umc_6th_wit_android.data.local.CategoryDto
import com.umc.umc_6th_wit_android.databinding.FragmentRankingCategoryBinding


class RankingCategoryFragment : Fragment() {
    lateinit var binding: FragmentRankingCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankingCategoryBinding.inflate(inflater, container, false)
        val items = CategoryDao().items

        //test data
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩"))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩"))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩"))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩"))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩"))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩"))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩"))

        val adapter = RankingCategoryRVAdapter(items)
        binding.rankingCategoryRv.adapter = adapter
        binding.rankingCategoryRv.layoutManager  = GridLayoutManager(context, 2)

        return binding.root
    }
}