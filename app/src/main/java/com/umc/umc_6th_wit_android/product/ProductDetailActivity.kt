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
import com.umc.umc_6th_wit_android.wish.CartItem
import com.umc.umc_6th_wit_android.wish.WishBoardListDelRequest
import com.umc.umc_6th_wit_android.wish.WishItem
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

        // UI 초기화 부분
        binding.comparisonBtnIv.setOnClickListener {
            val intent = Intent(this, PriceActivity::class.java)
            startActivity(intent)
        }

        binding.heartBtnIv.setOnClickListener {
            // 하트 버튼 이미지 변경 로직
            if (!isHelpIv) {
                val request = WishBoardListDelRequest(
                    product_ids = listOf(1)   //product_id 넣기
                )
                val productService = ProductService(this@ProductDetailActivity)
                productService.setProductDetailView(this)
                productService.delCart(request)
                binding.heartBtnIv.setImageResource(R.drawable.heart_btn_empty_image)
            } else {
                val productService = ProductService(this@ProductDetailActivity)
                productService.setProductDetailView(this)
                productService.addCart(1)   //product_id 넣기
                binding.heartBtnIv.setImageResource(R.drawable.heart_btn_full_image)
            }
            isHelpIv = !isHelpIv // 하트 버튼 상태 변경

            // DB 하트 숫자 변경 (여기에 DB 처리 로직 추가)
        }

    }

    override fun onResume() {
        super.onResume()
        getRecommendProducts()
    }

    private fun getRecommendProducts() {
        val productService = ProductService(this@ProductDetailActivity)
        productService.setProductDetailView(this)

        productService.getProductDetail(1)   //product_id 넣기
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
        isHelpIv = result.is_heart == 1
        if (!isHelpIv) {
            binding.heartBtnIv.setImageResource(R.drawable.heart_btn_empty_image)
        } else {
            binding.heartBtnIv.setImageResource(R.drawable.heart_btn_full_image)
        }
    }

    override fun onGetProductFailure(code: String, message: String) {
        Log.d("Product-FAILURE", code)
    }

    override fun onPostAddCartSuccess(code: String, response: CartItem) {
        TODO("Not yet implemented")
    }

    override fun onPostAddCartFailure(code: String, error: String) {
        TODO("Not yet implemented")
    }

    override fun onPostDelCartSuccess(code: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun onPostDelCartFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }

}
