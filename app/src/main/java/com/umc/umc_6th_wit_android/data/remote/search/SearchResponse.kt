package com.umc.umc_6th_wit_android.data.remote.search

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName (value = "isSuccess") val isSuccess: Boolean,
    @SerializedName (value = "code") val code:String,
    @SerializedName (value = "message") val message:String,
    @SerializedName (value = "result") val result: SearchResult
)

/*data class SearchResult(
    @SerializedName (value = "total") val total: Int,
    @SerializedName (value = "page") val code:Int,
    @SerializedName (value = "limit") val limit:Int,
    @SerializedName (value = "products") val souvenirs: List<Souvenir>
)*/
//서버 배포수정하면 위에서 아래걸로 수정하면 됨.
data class SearchResult(
    @SerializedName (value = "total") val total: Int,
    @SerializedName (value = "products") val souvenirs: List<Souvenir>,
    @SerializedName (value = "nextCursor") val nextCursor: Int
)
data class Souvenir(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("won_price") val wonPrice: Int,
    @SerializedName("en_price") val enPrice: Int,
    @SerializedName("image") val imageUrl: String,
    @SerializedName("reviews") val reviewCount: Int,
    @SerializedName("rating") val rating: Float,
    @SerializedName("is_heart") var isHeart: Int,
    @SerializedName("row_num") val rowNum: Int
    )

data class RecentSearchResponse(
    @SerializedName (value = "isSuccess") val isSuccess: Boolean,
    @SerializedName (value = "code") val code:String,
    @SerializedName (value = "message") val message:String,
    @SerializedName (value = "result") val result: List<String>
)
//최근 검색어 삭제
data class RecentDeleteResponse(
    @SerializedName (value = "isSuccess") val isSuccess: Boolean,
    @SerializedName (value = "code") val code:String,
    @SerializedName (value = "message") val message:String,
)

data class PopularSearchResponse(
    @SerializedName (value = "isSuccess") val isSuccess: Boolean,
    @SerializedName (value = "code") val code:String,
    @SerializedName (value = "message") val message:String,
    @SerializedName (value = "result") val result: List<String>
)

