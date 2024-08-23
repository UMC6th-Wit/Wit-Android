package com.umc.umc_6th_wit_android.product

import com.umc.umc_6th_wit_android.data.remote.product.ProductResult
import com.umc.umc_6th_wit_android.data.remote.product.RatingStatsResult

interface ProductView {

    //상품 상세 정보 가져오기
    fun onGetProductSuccess(code: String, result: ProductResult)
    fun onGetProductFailure(code: String, message: String)

}