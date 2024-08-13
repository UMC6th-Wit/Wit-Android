package com.umc.umc_6th_wit_android.home.ranking

import AgeFragment
import GenderFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class RankingVpAdapter(activity: FragmentActivity, private val gender: String, private val age: String, private val period: String) : FragmentStateAdapter(activity) {
    private val fragments = arrayOf(
        GenderFragment.newInstance(gender),
        AgeFragment.newInstance(age),
        PeriodFragment.newInstance(period)
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    // 특정 프래그먼트에 접근하는 메서드
    fun getFragment(position: Int): Fragment? {
        return if (position in fragments.indices) fragments[position] else null
    }
}