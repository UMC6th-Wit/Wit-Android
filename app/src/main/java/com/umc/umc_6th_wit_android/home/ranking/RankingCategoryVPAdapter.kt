package com.umc.umc_6th_wit_android.home.ranking

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RankingCategoryVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0->RankingCategoryFragment()
            else -> RankingCategoryFragment()
        }
    }
}