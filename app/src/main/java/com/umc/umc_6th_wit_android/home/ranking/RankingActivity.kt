package com.umc.umc_6th_wit_android.home.ranking

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.umc_6th_wit_android.databinding.ActivityPriceBinding
import com.umc.umc_6th_wit_android.databinding.ActivityRankingBinding
import com.umc.umc_6th_wit_android.home.HomeVPAdapter

class RankingActivity : AppCompatActivity() {
    lateinit var binding: ActivityRankingBinding
    private val title = arrayListOf("성별", "연령대", "기간")

    private lateinit var rankingAdapter: RankingVpAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingBinding.inflate(layoutInflater)

        rankingAdapter = RankingVpAdapter(this)
        binding.rankingVp.adapter = rankingAdapter

        TabLayoutMediator(binding.rankingTb, binding.rankingVp) {//TabLayout와 Vp를 연결하는 중재자
                tab, position ->
            tab.text = title[position]
        }.attach()

        setContentView(binding.root)
    }
}