package com.umc.umc_6th_wit_android.home.ranking

import AgeFragment
import GenderFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RankingVpAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GenderFragment() // 성별
            1 -> AgeFragment() // 연령대
            2 -> PeriodFragment() // 기간
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}