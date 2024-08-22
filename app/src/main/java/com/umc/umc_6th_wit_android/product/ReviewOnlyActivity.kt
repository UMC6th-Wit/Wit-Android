package com.umc.umc_6th_wit_android.product

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.PriceActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.ActivityReviewOnlyBinding
import com.umc.umc_6th_wit_android.home.ProductDetailFragment

class ReviewOnlyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewOnlyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewOnlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //우측 상단 리뷰 쓰기 버튼 -> activty_review_write 로 이동
        binding.goToReviewWriteBtnTv.setOnClickListener {
            val intent = Intent(this, ReviewWriteActivity::class.java)
            startActivity(intent)
        }

        //Activity 내에 Fragment 적용
        // 처음에는 FirstFragment(베스트순)
        if (savedInstanceState == null) {
            replaceFragment(ReviewBestFragment())
        }

        binding.reviewSequenceBestBtnTv.setOnClickListener {
            // 베스트순 클릭 시 Fragment로 교체
            replaceFragment(ReviewBestFragment())
        }

        binding.reviewSequenceRecentBtnTv.setOnClickListener {
            // 최신 클릭 시 Fragment로 교체
            replaceFragment(ReviewNewFragment())
        }

    }

    //Fragment 변경 로직 관련 함수
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.review_sequence_rv, fragment)
            .addToBackStack(null)
            .commit()
    }


}