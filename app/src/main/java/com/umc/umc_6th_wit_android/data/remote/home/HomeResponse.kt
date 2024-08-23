package com.umc.umc_6th_wit_android.data.remote.home

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.umc.umc_6th_wit_android.data.remote.notice.Notice
import kotlinx.parcelize.Parcelize

data class HomeResponse(
    @SerializedName(value = "isSuccess") val isSuccess: Boolean,
    @SerializedName(value = "code") val code: String,
    @SerializedName(value = "message") val message: String,
    @SerializedName(value = "result") val result: HomeResult
)

data class HomeResult(
    @SerializedName(value = "user") val user: User,
    @SerializedName(value = "recommendations") val recommendations: List<Product>,
    @SerializedName(value = "popularProducts") val popularProducts: Map<String, List<ProductVer2>>,
    @SerializedName(value = "nyamRecommendations") val nyamRecommendations: List<Product>,
    @SerializedName(value = "notices") val notices: List<Notice>
)

data class HomePersonalResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: HomePersonalResult
)

data class HomePersonalResult(
    @SerializedName(value = "user") val user: User,
    @SerializedName("recommendations") val recommendations: List<Product>
)

data class HomeBestFoodResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: HomeBestFoodResult
)

data class HomeBestFoodResult(
    @SerializedName("nyamRecommendations") val nyamRecommendations: List<Product>
)


// 'user' 객체에 대한 데이터 클래스
data class User(
    @SerializedName(value = "user_id") val userId: Int,
    @SerializedName(value = "username") val username: String,
    @SerializedName(value = "usernickname") val userNickname: String
)

// 'Product' 객체에 대한 데이터 클래스
data class Product(
    @SerializedName(value = "product_id") val id: Int,
    @SerializedName(value = "product_name") val name: String,
    @SerializedName(value = "won_price") val wonPrice: Int,
    @SerializedName(value = "en_price") val enPrice: Int,
    @SerializedName(value = "image") val imageUrl: String,
    @SerializedName(value = "wish_count") val wishCount: Int,
    @SerializedName(value = "main_category_name") val mainCategoryName: String,
    @SerializedName(value = "is_heart") var isHeart: Int,
    @SerializedName(value = "review_count") val reviewCount: Int,
    @SerializedName(value = "average_rating") val rating: Float
)
@Parcelize
data class ProductVer2(
    @SerializedName(value = "id") val id: Int,
    @SerializedName(value = "name") val name: String,
    @SerializedName(value = "won_price") val wonPrice: Int,
    @SerializedName(value = "en_price") val enPrice: Int,
    @SerializedName(value = "image") val imageUrl: String,
    @SerializedName(value = "sales_area") val salesArea: String,
    @SerializedName(value = "wish_count") val wishCount: Int,
    @SerializedName(value = "is_heart") var isHeart: Boolean,
    @SerializedName(value = "review_count") val reviewCount: Int,
    @SerializedName(value = "review_avg") val rating: Float
) : Parcelable

//카테고리
data class CategoryResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: CategoryResult
)
data class CategoryResult(
    @SerializedName(value = "user") val user: User,
    @SerializedName("groupedProducts") val popularProducts: Map<String, List<ProductVer2>>?,
    @SerializedName(value = "cursor") val nextCursor: Int? = 0,
)


