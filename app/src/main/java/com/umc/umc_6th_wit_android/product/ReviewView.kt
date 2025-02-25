package com.umc.umc_6th_wit_android.product

import com.umc.umc_6th_wit_android.data.remote.product.HelpfulResponse
import com.umc.umc_6th_wit_android.data.remote.product.RatingStatsResult
import com.umc.umc_6th_wit_android.data.remote.product.ReviewsResult

interface ReviewView {

    // 제품 리뷰 목록 불러오기 (베스트순, 최신순)
    fun onGetReviewsSuccess(code: String, result: ReviewsResult)
    fun onGetReviewsFailure(code: String, message: String)

    // 상품 별점 불러오기 (별점별 리뷰 개수)
    fun onGetRatingStatsSuccess(code: String, result: RatingStatsResult)
    fun onGetRatingStatsFailure(code: String, message: String)

    // 리뷰에 도움이 돼요 누르기
    fun onPostHelpfulSuccess(code: String, result: HelpfulResponse)
    fun onPostHelpfulFailure(code: String, message: String)
}