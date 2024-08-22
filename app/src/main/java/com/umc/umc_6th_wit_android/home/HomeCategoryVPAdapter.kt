package com.umc.umc_6th_wit_android.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.umc_6th_wit_android.data.remote.home.ProductVer2

class HomeCategoryVPAdapter(fragment: Fragment, private val products : Map<String, List<ProductVer2>>) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    // 카테고리 키 리스트를 정의합니다.
    private val categories = listOf("ALL", "식품", "뷰티코스메틱", "의약품", "생활용품")
    override fun createFragment(position: Int): Fragment {
        // 각 카테고리에 해당하는 키를 가져옵니다.
        val categoryKey = categories[position]

        // HomeCategoryFragment에 전달할 Bundle을 생성합니다.
        val fragment = HomeCategoryFragment()
        fragment.arguments = Bundle().apply {
            putString("categoryKey", categoryKey)
            putParcelableArrayList("productList", ArrayList(products[categoryKey]))
        }

        return fragment
    }
}