package com.umc.umc_6th_wit_android.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.umc_6th_wit_android.LocationActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.local.PopupDto
import com.umc.umc_6th_wit_android.databinding.FragmentHomeBinding
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate

class HomeFragment : Fragment(), HomePopupRVAdapter.OnItemRemovedListener {
    lateinit var binding: FragmentHomeBinding

    private val title = arrayListOf("홈", "랭킹")

    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())
    private val items = ArrayList<PopupDto>()

    private lateinit var homeAdapter: HomeVPAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        homeAdapter = HomeVPAdapter(this)
        binding.homeVp.adapter = homeAdapter
        binding.homeVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.homeSv.smoothScrollTo(0, 0)
                updatePagerHeightForFragment(position)
            }
        })
        TabLayoutMediator(binding.homeTb, binding.homeVp) {//TabLayout와 Vp를 연결하는 중재자
                tab, position ->
            tab.text = title[position]
        }.attach()

        // 초기 높이 설정
        binding.homeVp.post {
            updatePagerHeightForFragment(binding.homeVp.currentItem)
        }

        //최상단 스크롤 이동



        val panelAdapter = PanelVPAdapter(this)
        panelAdapter.addFragment(PanelFragment(R.drawable.panel1, "일본여행 기념품으로\n꼭 사야한다는 화제의 디저트", "먼작귀 X 도쿄바나나"))
        panelAdapter.addFragment(PanelFragment(R.drawable.panel1, "일본여행 기념품으로\n꼭 사야한다는 화제의 디저트", "먼작귀 X 도쿄바나나"))
        panelAdapter.addFragment(PanelFragment(R.drawable.panel1, "일본여행 기념품으로\n꼭 사야한다는 화제의 디저트", "먼작귀 X 도쿄바나나"))
        binding.homePanelVp.adapter = panelAdapter
        binding.homePanelVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        // Indicator에 viewPager 설정
        binding.homePanelIndicator.setViewPager(binding.homePanelVp)


        startAutoSlide(panelAdapter)

        // 팝업 아이템 초기화
        items.add(PopupDto("위트 서비스 및 이용 가이드 안내",9))
        items.add(PopupDto("위트 서비스 및 이용 가이드 안내",9))
        items.add(PopupDto("위트 서비스 및 이용 가이드 안내",9))

        // 리사이클러뷰 설정
        val adapter = HomePopupRVAdapter(items)
        adapter.itemRemovedListener = this

        binding.homeRv.adapter = adapter
        binding.homeRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL,false)

        updateItemCount()

        // 팝업 알림 클릭시
        binding.cvAlarm.setOnClickListener {
            togglePopupVisibility()
        }

        binding.locationBtn.setOnClickListener {
            val intent = Intent(activity, LocationActivity::class.java)
            startActivity(intent)
        }


        return binding.root
    }
    private fun togglePopupVisibility() {
        if (binding.popupBg.visibility == View.GONE) {
            binding.popupBg.visibility = View.VISIBLE
            binding.homeRv.visibility = View.VISIBLE
            binding.tv.visibility = View.VISIBLE
        } else {
            binding.popupBg.visibility = View.GONE
            binding.homeRv.visibility = View.GONE
            binding.tv.visibility = View.GONE
        }
    }
    // 아이템 제거 콜백 메서드
    override fun onItemRemoved(newCount: Int) {
        // alarm_num TextView 또는 다른 UI 요소 업데이트
        binding.alarmNum.text = newCount.toString()
        binding.alarmNumCv.visibility = if (items.size > 0) View.VISIBLE else View.GONE
    }

    private fun updateItemCount() {
        binding.alarmNum.text = items.size.toString()
        binding.alarmNumCv.visibility = if (items.size > 0) View.VISIBLE else View.GONE
    }
    override fun onResume() {
        super.onResume()
        // 프래그먼트가 화면에 표시될 때 스크롤 위치를 최상단으로 이동
        binding.homeSv.smoothScrollTo(0, 0)
        // 또는 부드럽게 이동하려면 다음을 사용:
        // scrollView.smoothScrollTo(0, 0);
        updateItemCount() // 화면에 보일 때마다 아이템 개수 업데이트
    }
    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel() // Timer 해제
    }
    fun navigateToRanking() {
        if ( binding.homeVp != null) {
            binding.homeVp.setCurrentItem(1, true)
        }
    }
    private fun updatePagerHeightForFragment(position: Int) {
        val fragment = homeAdapter.getFragment(position)
        fragment?.view?.let { view ->
            view.post {
                val height = view.measuredHeight
                val layoutParams = binding.homeVp.layoutParams
                layoutParams.height = height
                binding.homeVp.layoutParams = layoutParams
                binding.homeVp.requestLayout() // 기존 코드와 동일
            }
        }
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