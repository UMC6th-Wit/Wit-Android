package com.umc.umc_6th_wit_android.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentHomeBinding
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate

class HomeFragment : Fragment(){
    lateinit var binding: FragmentHomeBinding

    private val title = arrayListOf("홈", "랭킹")


    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        val homeAdapter = HomeVPAdapter(this)
        binding.homeVp.adapter = homeAdapter
        TabLayoutMediator(binding.homeTb, binding.homeVp) {//TabLayout와 Vp를 연결하는 중재자
                tab, position ->
            tab.text = title[position]
        }.attach()


        val panelAdapter = PanelVPAdapter(this)
        panelAdapter.addFragment(PanelFragment(R.drawable.panel1, "일본여행 기념품으로\n꼭 사야한다는 화제의 디저트", "먼작귀 X 도쿄바나나"))
        panelAdapter.addFragment(PanelFragment(R.drawable.panel1, "일본여행 기념품으로\n꼭 사야한다는 화제의 디저트", "먼작귀 X 도쿄바나나"))
        panelAdapter.addFragment(PanelFragment(R.drawable.panel1, "일본여행 기념품으로\n꼭 사야한다는 화제의 디저트", "먼작귀 X 도쿄바나나"))
        binding.homePanelVp.adapter = panelAdapter
        binding.homePanelVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        // Indicator에 viewPager 설정
        binding.homePanelIndicator.setViewPager(binding.homePanelVp)

        startAutoSlide(panelAdapter)

        return binding.root
    }
    private fun startAutoSlide(adpater : PanelVPAdapter) {
        // 일정 간격으로 슬라이드 변경 (3초마다)
        timer.scheduleAtFixedRate(3000, 3000) {
            handler.post {
                val nextItem = binding.homePanelVp.currentItem + 1
                if (nextItem < adpater.itemCount) {
                    binding.homePanelVp.currentItem = nextItem
                } else {
                    binding.homePanelVp.currentItem = 0 // 마지막 페이지에서 첫 페이지로 순환
                }
            }
        }
    }
}