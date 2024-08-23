package com.umc.umc_6th_wit_android.product

import com.umc.umc_6th_wit_android.data.remote.product.ReviewCreationResult

interface ReviewCreationView {
    // 리뷰 작성하기 (이미지 포함)
    fun onPostReviewCreationSuccess(code: String, result: ReviewCreationResult)
    fun onPostReviewCreationFailure(code: String, message: String)
}