package com.umc.umc_6th_wit_android.product

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.umc_6th_wit_android.PriceActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.ActivityProductDetailBinding
import com.umc.umc_6th_wit_android.home.ProductDetailFragment

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private var isHelpIv = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Activity 내에 Fragment 적용
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_product_detail, ProductDetailFragment())
                .commit()
        }

        // UI 초기화 부분
        binding.comparisonBtnIv.setOnClickListener {
            val intent = Intent(this, PriceActivity::class.java)
            startActivity(intent)
        }

        binding.heartBtnIv.setOnClickListener {
            // 하트 버튼 이미지 변경 로직
            if (isHelpIv) {
                binding.heartBtnIv.setImageResource(R.drawable.heart_btn_empty_image)
            } else {
                binding.heartBtnIv.setImageResource(R.drawable.heart_btn_full_image)
            }
            isHelpIv = !isHelpIv // 하트 버튼 상태 변경

            // DB 하트 숫자 변경 (여기에 DB 처리 로직 추가)
        }

    }
}
