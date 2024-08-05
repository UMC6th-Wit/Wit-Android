package com.umc.umc_6th_wit_android.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.local.CategoryDao
import com.umc.umc_6th_wit_android.data.local.CategoryDto
import com.umc.umc_6th_wit_android.databinding.FragmentSearchRsltBinding
import com.umc.umc_6th_wit_android.home.CustomRVAdapter
import com.umc.umc_6th_wit_android.home.ProductDetailActivity

class SearchRsltFragment : Fragment() {
    lateinit var binding: FragmentSearchRsltBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchRsltBinding.inflate(layoutInflater)

        // arguments에서 검색어 데이터 가져오기
        val searchQuery = arguments?.getString("searchQuery")
        binding.etSearch.setText(searchQuery)

        val items = CategoryDao().items

        //test data
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))

        val adapter = CustomRVAdapter(items)
        adapter.setOnItemClickListener(object : CustomRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(activity, ProductDetailActivity::class.java)
                startActivity(intent)
//                changeActivity(items[position])
            }
        })
        binding.rsltRv.adapter = adapter
        binding.rsltRv.layoutManager  = GridLayoutManager(context, 2)

        return binding.root
    }


}