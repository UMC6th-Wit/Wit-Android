package com.umc.umc_6th_wit_android.data.remote.product

import retrofit2.Call
import retrofit2.http.*

interface ProductRetrofitInterface {

    // 제품 상세 정보 불러오기
    @GET("/products/{productId}")
    fun getProductDetail(
        @Path("productId") productId: Int
    ): Call<ProductResponse>

    // 새로운 리뷰 목록 불러오기
    @GET("/products/{productId}/reviews/overview")
    fun getReviewOverview(
        @Path("productId") productId: Int
    ): Call<NewReviewOverviewResponse>

    // 제품 리뷰 목록 불러오기 (베스트순, 최신순)
    @GET("/products/{productId}/reviews")
    fun getProductReviews(
        @Path("productId") productId: Int
    ): Call<ProductReviewsResponse>

    // 리뷰 작성하기 (이미지 포함)
    @POST("/products/{productId}/reviews")
    @FormUrlEncoded
    fun createReview(
        @Path("productId") productId: Int,
        @Field("rating") rating: Int,
        @Field("content") content: String,
        @Field("images") images: String
    ): Call<ReviewCreationResponse>

    // 리뷰 작성 페이지 불러오기
    @GET("/products/{productId}/reviews/new")
    fun getReviewPageData(
        @Path("productId") productId: Int
    ): Call<ReviewPageDataResponse>

    // 상품 별점 불러오기 (별점별 리뷰 개수)
    @GET("/products/{productId}/rating-stats")
    fun getRatingStats(
        @Path("productId") productId: Int
    ): Call<RatingStatsResponse>

    // 리뷰에 도움이 돼요 누르기
    @POST("/products/{productId}/reviews/{reviewId}/helpful")
    fun markReviewAsHelpful(
        @Path("productId") productId: Int,
        @Path("reviewId") reviewId: Int
    ): Call<HelpfulResponse>
}

