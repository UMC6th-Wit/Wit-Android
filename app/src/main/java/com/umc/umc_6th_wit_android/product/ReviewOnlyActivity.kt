package com.umc.umc_6th_wit_android.product

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.PriceActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.remote.product.HelpfulResponse
import com.umc.umc_6th_wit_android.data.remote.product.ProductService
import com.umc.umc_6th_wit_android.data.remote.product.RatingStatsResult
import com.umc.umc_6th_wit_android.data.remote.product.ReviewsResult
import com.umc.umc_6th_wit_android.databinding.ActivityReviewOnlyBinding
import com.umc.umc_6th_wit_android.home.ProductDetailFragment

class ReviewOnlyActivity : AppCompatActivity(), ReviewView {

    private lateinit var binding: ActivityReviewOnlyBinding
    private var productId: Int? = null  // id를 저장할 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewOnlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Intent에서 id 값을 추출하여 저장
        productId = intent.getIntExtra("id", -1) // "id"가 없는 경우 기본값 -1로 설정

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
            binding.reviewSequenceBestBtnTv.setTextColor(Color.parseColor("#2572F6"))
            binding.reviewSequenceRecentBtnTv.setTextColor(Color.parseColor("#9A9A9A"))
        }

        binding.reviewSequenceRecentBtnTv.setOnClickListener {
            // 최신순 클릭 시 Fragment로 교체
            replaceFragment(ReviewNewFragment())
            binding.reviewSequenceRecentBtnTv.setTextColor(Color.parseColor("#2572F6"))
            binding.reviewSequenceBestBtnTv.setTextColor(Color.parseColor("#9A9A9A"))
        }

    }

    //Fragment 변경 로직 관련 함수
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.review_sequence_rv, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        getRatingStars()
    }
    private fun getRatingStars() {
        Log.d("ReviewMinFragment", "Ok id received")
        val productService = ProductService(this)
        productService.setReviewView(this)
        productService.getRatingStats(productId!!)
    }
    override fun onGetRatingStatsSuccess(code: String, result: RatingStatsResult) {
        Log.d("getRatingStars-SUCCESS", result.toString())
        binding.review5NumberTv.text = (result.rating5Count ?: 0).toString()
        binding.review4NumberTv.text = (result.rating4Count ?: 0).toString()
        binding.review3NumberTv.text = (result.rating3Count ?: 0).toString()
        binding.review2NumberTv.text = (result.rating2Count ?: 0).toString()
        binding.review1NumberTv.text = (result.rating1Count ?: 0).toString()

    }

    override fun onGetRatingStatsFailure(code: String, message: String) {
        Log.d("getRatingStars-FAILURE", message)
    }

    override fun onGetReviewsSuccess(code: String, result: ReviewsResult) {
        TODO("Not yet implemented")
    }

    override fun onGetReviewsFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }



    override fun onPostHelpfulSuccess(code: String, result: HelpfulResponse) {
        TODO("Not yet implemented")
    }

    override fun onPostHelpfulFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }


}