package com.umc.umc_6th_wit_android.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HomeVPAdapter (fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return  when(position){
            0 -> SubHomeFragment()
            else -> LankingFragment()
        }
    }
}