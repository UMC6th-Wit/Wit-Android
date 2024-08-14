package com.umc.umc_6th_wit_android.home.ranking

import AgeFragment
import GenderFragment
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.umc_6th_wit_android.databinding.ActivityRankingBinding

class RankingActivity : AppCompatActivity(),
    GenderFragment.OnGenderSelectedListener,
    AgeFragment.OnAgeSelectedListener,
    PeriodFragment.OnPeriodSelectedListener {

    lateinit var binding: ActivityRankingBinding
    private val title = arrayListOf("성별", "연령대", "기간")
    private lateinit var rankingAdapter: RankingVpAdapter
    private var gender: String = "성별 전체"
    private var age: String = "연령대 전체"
    private var period: String = "실시간 랭킹"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRankingBinding.inflate(layoutInflater)

        // SharedPreferences에서 상태 복원
        loadStateFromPreferences()

        rankingAdapter = RankingVpAdapter(this, gender, age, period)
        binding.rankingVp.adapter = rankingAdapter

        TabLayoutMediator(binding.rankingTb, binding.rankingVp) { tab, position ->
            tab.text = title[position]
        }.attach()

        // Ranking Fragment에서 전달된 탭 인덱스를 가져옴
        val tabIndex = intent.getIntExtra("TAB_INDEX", 0)  // 기본값은 0 (성별 탭)
        binding.rankingVp.currentItem = tabIndex  // ViewPager의 현재 아이템(페이지)설정
        
        
        //랭킹 보기 버튼 클릭시 액티비티 종료
        binding.rankingBtn.setOnClickListener {
            finish()
        }

        //초기화 버튼/ic 클릭 -> reset
        binding.icReset.setOnClickListener {
            resetToDefaults()
        }
        binding.tvReset.setOnClickListener {
            resetToDefaults()
        }


        // 복원된 데이터로 UI 업데이트
        updateUI()

        setContentView(binding.root)
    }

    override fun onPause() {
        super.onPause()
        saveStateToPreferences() // 상태 변경 시 저장
    }
    override fun onGenderSelected(gender: String) {
        this.gender = gender
        binding.genderTv.text = gender
    }

    override fun onAgeSelected(age: String) {
        this.age = age
        binding.ageTv.text = age
    }

    override fun onPeriodSelected(period: String) {
        this.period = period
        binding.periodTv.text = period
    }

    // 상태를 SharedPreferences에 저장
    private fun saveStateToPreferences() {
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("gender", gender)
            putString("age", age)
            putString("period", period)
            apply()
        }
        Log.d("RankingActivity", "State saved to preferences: Gender = $gender")
    }

    // SharedPreferences에서 상태를 로드
    private fun loadStateFromPreferences() {
        val sharedPreferences = getPreferences(MODE_PRIVATE)
        gender = sharedPreferences.getString("gender", "성별 전체") ?: "성별 전체"
        age = sharedPreferences.getString("age", "연령대 전체") ?: "연령대 전체"
        period = sharedPreferences.getString("period", "실시간 랭킹") ?: "실시간 랭킹"
        Log.d("RankingActivity", "State loaded from preferences: Gender = $gender")
    }

    // UI 업데이트 함수
    private fun updateUI() {
        binding.genderTv.text = gender
        binding.ageTv.text = age
        binding.periodTv.text = period
    }
    // 모든 값을 기본값으로 초기화
    private fun resetToDefaults() {
        gender = "성별 전체"
        age = "연령대 전체"
        period = "실시간 랭킹"
        updateUI()
        resetFragmentUI()
    }
    // 프래그먼트 UI 리셋
    private fun resetFragmentUI() {
        (rankingAdapter.getFragment(0) as? GenderFragment)?.resetUI()
        (rankingAdapter.getFragment(1) as? AgeFragment)?.resetUI()
        (rankingAdapter.getFragment(2) as? PeriodFragment)?.resetUI()
    }
}
