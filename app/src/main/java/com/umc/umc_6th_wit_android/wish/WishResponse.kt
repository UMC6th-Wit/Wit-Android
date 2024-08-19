package com.umc.umc_6th_wit_android.wish

import com.google.gson.annotations.SerializedName
import java.util.Date

data class WishResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: WishItemResult
)

data class WishItemResult(
    @SerializedName("userId") val userId: Int,
    @SerializedName("cart_Products") val cart_Products: List<WishItem>,
    //시간 부분은 String로 바꿔야할수도 있음
    @SerializedName("created_at") val created_at: Date,
    @SerializedName("updated_at") val updated_at: Date
)

data class WishBoardResponse(
//    @SerializedName("isSuccess") val isSuccess: Boolean,
//    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: WishBoardItemResult
)

data class WishBoardItemResult(
    /*@SerializedName("board_Products")*/ val board_Products: List<Wishboard>,  //해당 부분 명칭이 필요한가?
    //시간 부분은 String로 바꿔야할수도 있음
    @SerializedName("created_at") val created_at: Date,
    @SerializedName("updated_at") val updated_at: Date
)

data class WishBoardNameResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("folder_Name") val folder_Name: String
)

data class WishBoardDeleteResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)

data class WishListCreateRequest(
    val product_ids: List<Int>,
    val folder_name: String
)