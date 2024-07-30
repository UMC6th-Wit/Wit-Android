package com.umc.umc_6th_wit_android.home

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.umc.umc_6th_wit_android.home.ranking.RankingFragment

class HomeVPAdapter (fragment: Fragment) : FragmentStateAdapter(fragment){
    private val registeredFragments = SparseArray<Fragment>()
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        val fragment = when(position) {
            0 -> SubHomeFragment()
            else -> RankingFragment()
        }
        registeredFragments.put(position, fragment)
        return fragment
    }
    fun getFragment(position: Int): Fragment? = registeredFragments.get(position)
}