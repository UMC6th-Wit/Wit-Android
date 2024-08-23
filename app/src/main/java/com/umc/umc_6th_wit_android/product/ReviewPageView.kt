package com.umc.umc_6th_wit_android.product

import com.umc.umc_6th_wit_android.data.remote.product.ReviewPageDataResult

interface ReviewPageView {

    // 리뷰 작성 페이지 불러오기
    fun onGetReviewPageDataSuccess(code: String, result: ReviewPageDataResult)
    fun onGetReviewPageDataFailure(code: String, message: String)

}