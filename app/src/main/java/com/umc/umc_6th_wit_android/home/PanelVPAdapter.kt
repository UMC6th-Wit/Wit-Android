package com.umc.umc_6th_wit_android.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PanelVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragmentlist : ArrayList<Fragment> = ArrayList()
    override fun getItemCount(): Int = fragmentlist.size//VP에 데이터 몇개 전달 할 것인가 = {return framentList.size}랑 같음

    override fun createFragment(position: Int): Fragment = fragmentlist[position]//Fragment 생성 함수 position에는 0~getItemCount()까지 들어옴

    fun addFragment(fragment: Fragment){
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size-1)//VP에 fragment추가 됐으니 띄워줘라고 하는 느낌
    }

}