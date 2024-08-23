package com.umc.umc_6th_wit_android.product

import com.umc.umc_6th_wit_android.data.remote.product.HelpfulResponse
import com.umc.umc_6th_wit_android.data.remote.product.ReviewCreationResult
import com.umc.umc_6th_wit_android.data.remote.product.ReviewOverviewResult
import com.umc.umc_6th_wit_android.data.remote.product.ReviewPageDataResult
import com.umc.umc_6th_wit_android.data.remote.product.ReviewsResult

interface ReviewView {

    // 새로운 리뷰 목록 불러오기
    fun onGetReviewOverviewSuccess(code: String, result: ReviewOverviewResult)
    fun onGetReviewOverviewFailure(code: String, message: String)

    // 제품 리뷰 목록 불러오기 (베스트순, 최신순)
    fun onGetReviewsSuccess(code: String, result: ReviewsResult)
    fun onGetReviewsFailure(code: String, message: String)

    // 리뷰 작성하기 (이미지 포함)
    fun onPostReviewCreationSuccess(code: String, result: ReviewCreationResult)
    fun onPostReviewCreationFailure(code: String, message: String)

    // 리뷰 작성 페이지 불러오기
    fun onGetReviewPageDataSuccess(code: String, result: ReviewPageDataResult)
    fun onGetReviewPageDataFailure(code: String, message: String)

    // 리뷰에 도움이 돼요 누르기
    fun onPostHelpfulSuccess(code: String, result: HelpfulResponse)
    fun onPostHelpfulFailure(code: String, message: String)
}