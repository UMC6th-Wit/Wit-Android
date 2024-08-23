package com.umc.umc_6th_wit_android.data.remote.product

import com.google.gson.annotations.SerializedName


//product detail
data class ProductResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ProductResult
)

data class ProductResult(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("product_type") val product_type: String,

    @SerializedName("won_price") val won_price: Int,
    @SerializedName("en_price") val en_price: Int,
    @SerializedName("image") val image: String,
    @SerializedName("sales_area") val sales_area: String,

    @SerializedName("manufacturing_country") val manufacturing_country: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("sub_category_id") val sub_category_id: Int,

    @SerializedName("user_id") val user_id: Int?,
    @SerializedName("is_heart") val is_heart: Int?,
    @SerializedName("heart_count") val heart_count: Int?,
    @SerializedName("average_rating") val average_rating: Double,
    @SerializedName("review_count") val review_count: Int,
    @SerializedName("latest_review_images") val latest_review_images: List<String>,
    @SerializedName("top_reviews") val top_reviews: List<Review>
)

//review
data class Review(
    @SerializedName("review_id") val review_id: Int,
    @SerializedName("rating") val rating: Double,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val created_at: String,

    @SerializedName("user_name") val user_name: String,
    @SerializedName("user_profile_image") val user_profile_image: String,
    @SerializedName("images") val images: String,
    @SerializedName("helpful_count") val helpful_count: Int
)

//새로운 리뷰 목록 불러오기
data class NewReviewOverviewResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ReviewOverviewResult
)

data class ReviewOverviewResult(
    @SerializedName("review_count") val reviewCount: Int,
    @SerializedName("average_rating") val averageRating: Double,
    @SerializedName("latest_images") val latestImages: List<String>,
    @SerializedName("top_helpful_reviews") val topHelpfulReviews: List<TopHelpfulReview>
)

data class TopHelpfulReview(
    @SerializedName("review_id") val reviewId: Int,
    @SerializedName("rating") val rating: Double,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_profile_image") val userProfileImage: String,
    @SerializedName("first_image") val firstImage: String,
    @SerializedName("helpful_count") val helpfulCount: Int
)

//제품 리뷰 목록 불러오기 (베스트순, 최신순)
data class ProductReviewsResponse(
    // @SerializedName("message") val message: String,
    @SerializedName("result") val result: ReviewsResult
)

data class ReviewsResult(
    @SerializedName("reviews") val reviews: List<Review>,
    @SerializedName("average_rating") val averageRating: Double,
    @SerializedName("total_reviews") val totalReviews: Int,
    @SerializedName("nextCursor") val nextCursor: Int
)

//리뷰 작성하기 (이미지 포함)
data class ReviewCreationResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ReviewCreationResult
)

data class RatingResponse(
    @SerializedName("rating") val rating: String?
)

data class ContentResponse(
    @SerializedName("content") val content: String?,
)

data class ReviewCreationResult(
    @SerializedName("review_id") val reviewId: Int,
    @SerializedName("rating") val rating: Double,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("user_profile_image") val userProfileImage: String,
    @SerializedName("images") val images: String
)

//리뷰 작성 페이지
data class ReviewPageDataResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ReviewPageDataResult
)

data class ReviewPageDataResult(
    @SerializedName("name") val name: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("content") val content: String?,
    @SerializedName("image") val image: List<String>
)

//상품 별점 불러오기 (별점별 리뷰 개수)
data class RatingStatsResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: RatingStatsResult
)

data class RatingStatsResult(
    @SerializedName("rating_5_count") val rating5Count: Int?,
    @SerializedName("rating_4_count") val rating4Count: Int?,
    @SerializedName("rating_3_count") val rating3Count: Int?,
    @SerializedName("rating_2_count") val rating2Count: Int?,
    @SerializedName("rating_1_count") val rating1Count: Int?
)

//리뷰에 도움이 돼요 누르기
data class HelpfulResponse(
    @SerializedName("message") val message: String,
    @SerializedName("helpful_count") val helpfulCount: Int?
)
