package com.umc.umc_6th_wit_android.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.internal.ViewUtils.dpToPx
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.umc_6th_wit_android.DetailActivity
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.local.PersonalDao
import com.umc.umc_6th_wit_android.databinding.FragmentHomeBinding
import com.umc.umc_6th_wit_android.databinding.FragmentSubhomeBinding
import ddwu.com.mobile.finalreport.data.PersonalDto

class SubHomeFragment : Fragment(){
    lateinit var binding: FragmentSubhomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubhomeBinding.inflate(inflater, container, false)

        val items = PersonalDao().items

        val adapter = PersonalRVAdapter(items)
        binding.personalRv.adapter = adapter
        binding.personalRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)


        adapter.setOnItemClickListener(object : PersonalRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(activity, DetailActivity::class.java)
                startActivity(intent)
//                changeActivity(items[position])
            }
        })

        val category : Array<String> = resources.getStringArray(R.array.CATEGORY)

        val rankingAdapter = TemplateCategoryVPAdapter(this)
        binding.templateCategoryVp.adapter = rankingAdapter

        TabLayoutMediator(binding.templateSelectCategoryTl, binding.templateCategoryVp){
                tab, position->
            tab.text= category[position]  //포지션에 따른 텍스트
        }.attach()  //탭레이아웃과 뷰페이저를 붙여주는 기능

        tabItemMargin(binding.templateSelectCategoryTl)


        return binding.root
    }
//    private fun changeActivity(personal : PersonalDto) {
//        val intent = Intent(activity, DetailActivity::class.java)
//        intent.putExtra("dto", personal)   // 클릭한 RecyclerView 항목 위치의 dto 전달
//        startActivity(intent)
//    }
    private fun tabItemMargin(mTabLayout : TabLayout){
        for (i in 0 until mTabLayout.getTabCount()) {
            val tab = (mTabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 20,0)
            tab.requestLayout()
        }
    }
}