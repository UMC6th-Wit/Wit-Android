package com.umc.umc_6th_wit_android.data.remote.product

import com.google.gson.annotations.SerializedName


data class ApiResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: Product
)

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val product_type: String,
    val won_price: Int,
    val en_price: Int,
    val image: String,
    val sales_area: String,
    val manufacturing_country: String,
    val created_at: String,
    val updated_at: String,
    val sub_category_id: Int,
    val user_id: Int?,
    val average_rating: Int,
    val review_count: Int,
    val latest_review_images: List<String>,
    val top_reviews: List<Review>
)

data class Review(
    val review_id: Int,
    val rating: Int,
    val content: String,
    val created_at: String,
    val user_name: String,
    val user_profile_image: String,
    val images: String,
    val helpful_count: Int
)
