package com.umc.umc_6th_wit_android.product

import com.umc.umc_6th_wit_android.data.remote.product.ReviewOverviewResult

interface ReviewOverviewView {

    // 새로운 리뷰 목록 불러오기
    fun onGetReviewOverviewSuccess(code: String, result: ReviewOverviewResult)
    fun onGetReviewOverviewFailure(code: String, message: String)

}