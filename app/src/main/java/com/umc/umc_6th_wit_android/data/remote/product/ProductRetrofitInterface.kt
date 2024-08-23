package com.umc.umc_6th_wit_android.data.remote.product

import com.umc.umc_6th_wit_android.wish.CartResponse
import com.umc.umc_6th_wit_android.wish.WishBoardDeleteResponse
import com.umc.umc_6th_wit_android.wish.WishBoardListDelRequest
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ProductRetrofitInterface {

    // 제품 상세 정보 불러오기
    @GET("products/{productId}")
    fun getProductDetail(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Path("productId") productId: Int
    ): Call<ProductResponse>

    // 새로운 리뷰 목록 불러오기
    @GET("products/{productId}/reviews/overview")
    fun getReviewOverview(
        @Path("productId") productId: Int
    ): Call<NewReviewOverviewResponse>

    // 제품 리뷰 목록 불러오기 (베스트순, 최신순)
    @GET("products/{productId}/reviews")
    fun getProductReviews(
        @Path("productId") productId: Int
    ): Call<ProductReviewsResponse>

    // 리뷰 작성하기 (이미지 포함)
    @POST("products/{productId}/reviews")
    @Multipart
    fun createReview(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Path("productId") productId: Int,
        @Part("rating") rating: RatingResponse,  // rating 값을 담는 RequestBody
        @Part("content") content: ContentResponse,  // content 값을 담는 RequestBody
        @Part("image") image: MultipartBody.Part
    ): Call<ReviewCreationResponse>

    // 리뷰 작성 페이지 불러오기
    @GET("products/{productId}/reviews/new")
    fun getReviewPageData(
        @Path("productId") productId: Int
    ): Call<ReviewPageDataResponse>

    // 상품 별점 불러오기 (별점별 리뷰 개수)
    @GET("products/{productId}/rating-stats")
    fun getRatingStats(
        @Path("productId") productId: Int
    ): Call<RatingStatsResponse>

    // 리뷰에 도움이 돼요 누르기
    @POST("products/{productId}/reviews/{reviewId}/helpful")
    fun markReviewAsHelpful(
        @Path("productId") productId: Int,
        @Path("reviewId") reviewId: Int
    ): Call<HelpfulResponse>

    //제품 장바구니에 담기
    @POST("product/add-cart/{product_id}")
    fun addCart(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Path("product_id") productId: Int,
    ): Call<CartResponse>

    //제품 장바구니에 빼기
    @POST("product/delete-cart")
    fun delCart(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Body request: WishBoardListDelRequest
    ): Call<WishBoardDeleteResponse>
}