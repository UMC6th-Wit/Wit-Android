package com.umc.umc_6th_wit_android.product

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.umc.umc_6th_wit_android.PriceActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.remote.home.HomeService
import com.umc.umc_6th_wit_android.data.remote.product.ProductResult
import com.umc.umc_6th_wit_android.data.remote.product.ProductService
import com.umc.umc_6th_wit_android.databinding.ActivityProductDetailBinding
import com.umc.umc_6th_wit_android.home.ProductDetailFragment
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.wish.WishService

class ProductDetailActivity : AppCompatActivity(), ProductView {

    lateinit var binding: ActivityProductDetailBinding
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

        val id = intent.getIntExtra("id", -1)  // 기본값을 -1로 설정

        if (id != -1) {
            // id가 정상적으로 전달되었다면 여기서 사용
            // 예: 서버에서 해당 id에 대한 정보를 불러오기
            Log.d("TargetActivity", "Received id: $id")
        } else {
            // id가 전달되지 않았을 경우의 처리
            Log.d("TargetActivity", "No id received")

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

        override fun onGetProductSuccess(code: String, result: ProductResult) {
            Log.d("Product-SUCCESS", code + result.name)

            //정보 가져 오는데 성공 -> 뷰에 반영
            val imageUri = Uri.parse(result.image)
            result.id
            binding.productDetailImg.setImageURI(imageUri)
            binding.productNameTv.text = "${result.name}"
            //binding.heartBtnTv.text = "${result.}" 제품 장바구니에 담기, 빼기 하트 숫자에 적용해야함
            binding.currencyYenTv.text = "${result.en_price}"
            binding.currencyWonTv.text = "${result.won_price}"
            binding.whereWidget1Tv.text = "${result.sales_area}"
            binding.whereWidget2Tv.text = "${result.sales_area}"
        }

        override fun onGetProductFailure(code: String, message: String) {
            Log.d("Product-FAILURE", code)
        }

    }
    override fun onResume() {
        super.onResume()
        getRecommendProducts()
    }

    private fun getRecommendProducts() {
        val productService = ProductService(this@ProductDetailActivity)
        productService.setProductDetailView(this)

        productService.getProductDetail(1)
    }

    override fun onGetProductSuccess(code: String, result: ProductResult) {
        Log.d("Product-SUCCESS", code + result.name)

        //정보 가져 오는데 성공 -> 뷰에 반영
        val imageUri = Uri.parse(result.image)
        binding.productDetailImg.let { imageView ->
            result.image?.let { imageUrl ->
                Glide.with(imageView.context)
                    .load(imageUrl)
                    .into(imageView)
            }
        }

        binding.productNameTv.text = "${result.name}"
        //binding.heartBtnTv.text = "${result.}" 제품 장바구니에 담기, 빼기 하트 숫자에 적용해야함
        binding.currencyYenTv.text = "${result.en_price}"
        binding.currencyWonTv.text = "${result.won_price}"
        binding.whereWidget1Tv.text = "${result.sales_area}"
        binding.whereWidget2Tv.text = "${result.sales_area}"
    }

    override fun onGetProductFailure(code: String, message: String) {
        Log.d("Product-FAILURE", code)
    }

}
