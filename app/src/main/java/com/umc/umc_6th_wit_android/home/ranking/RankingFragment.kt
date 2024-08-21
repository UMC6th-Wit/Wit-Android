package com.umc.umc_6th_wit_android.home.ranking

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentRankingBinding

class RankingFragment : Fragment() {

    lateinit var binding: FragmentRankingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankingBinding.inflate(inflater, container, false)

        val category : Array<String> = resources.getStringArray(R.array.CATEGORY)

        val rankingAdapter = RankingCategoryVPAdapter(this)
        binding.rankingCategoryVp.adapter = rankingAdapter

        TabLayoutMediator(binding.rankingCategoryTl, binding.rankingCategoryVp){
                tab, position->
            tab.text= category[position]  //포지션에 따른 텍스트
        }.attach()  //탭레이아웃과 뷰페이저를 붙여주는 기능

        tabItemMargin(binding.rankingCategoryTl)

        binding.gender.setOnClickListener {
            val intent = Intent(activity, RankingActivity::class.java)
            intent.putExtra("TAB_INDEX", 0)  // 성별 탭의 인덱스
            startActivity(intent)
        }

        binding.age.setOnClickListener {
            val intent = Intent(activity, RankingActivity::class.java)
            intent.putExtra("TAB_INDEX", 1)  // 연령대 탭의 인덱스
            startActivity(intent)
        }

        binding.period.setOnClickListener {
            val intent = Intent(activity, RankingActivity::class.java)
            intent.putExtra("TAB_INDEX", 2)  // 기간 탭의 인덱스
            startActivity(intent)
        }

        return binding.root
    }

    private fun tabItemMargin(mTabLayout : TabLayout){
        for (i in 0 until mTabLayout.getTabCount()) {
            val tab = (mTabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 30,0)
            tab.setPadding(40,0,40,0)
            tab.requestLayout()
        }
    }
}