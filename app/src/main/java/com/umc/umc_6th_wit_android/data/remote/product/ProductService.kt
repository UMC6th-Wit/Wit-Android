package com.umc.umc_6th_wit_android.data.remote.product

import android.content.Context
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.product.ProductDetailView
import com.umc.umc_6th_wit_android.product.ReviewView

class ProductService(private val context: Context) {

    // 뷰 지연 초기화 (lateinit)
    private lateinit var productDetailView: ProductDetailView
    private lateinit var reviewView: ReviewView

    // SharedPreferences 및 TokenManager 설정
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val tokenManager = TokenManager(sharedPreferences)

    // 뷰 설정 메소드
    fun setProductDetailView(view: ProductDetailView) {
        this.productDetailView = view
    }

    fun setReviewOverviewView(view: ReviewView) {
        this.reviewView= view
    }


}

