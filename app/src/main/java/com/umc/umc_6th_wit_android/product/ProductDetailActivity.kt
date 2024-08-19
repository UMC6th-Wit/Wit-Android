package com.umc.umc_6th_wit_android.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.ActivityProductDetailBinding
import com.umc.umc_6th_wit_android.product.ReviewMinActivity
import com.umc.umc_6th_wit_android.product.ReviewZeroActivity
import com.umc.umc_6th_wit_android.product.ProductDetailFragment


class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding

    private var reviewCount: Int = 0 // 현재 리뷰 개수, 페이지 로딩 시 마다

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_product_detail, ProductDetailFragment())
                .commit()
        }

        binding.productReviewSelectTv.setOnClickListener {
            if (reviewCount == 0) {
                val intent = Intent(this, ReviewZeroActivity::class.java)
                startActivity(intent)
            } else {
                // 리뷰가 0이 아닐 경우 리뷰가 있는 최소화 페이지와 연결
                val intent = Intent(this, ReviewMinActivity::class.java)
                startActivity(intent)
            }
        }
    }
}