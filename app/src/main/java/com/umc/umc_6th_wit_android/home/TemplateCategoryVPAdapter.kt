package com.umc.umc_6th_wit_android.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TemplateCategoryVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val categories = (1..5).toList()
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TemplateCategoryFragment()
            else -> TemplateCategoryFragment()
        }
    }
}