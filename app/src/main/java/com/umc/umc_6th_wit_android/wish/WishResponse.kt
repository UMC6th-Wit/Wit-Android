package com.umc.umc_6th_wit_android.wish

import com.google.gson.annotations.SerializedName
import java.util.Date

data class WishResponse(
//    @SerializedName("isSuccess") val isSuccess: Boolean,
//    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: WishItemResult
)

data class WishItemResult(
    @SerializedName("count") val count: Int,
    @SerializedName("products") val products: List<WishItem>,
    @SerializedName("nextCursor") val nextCursor: Int,
    //시간 부분은 String로 바꿔야할수도 있음
//    @SerializedName("created_at") val created_at: Date,
//    @SerializedName("updated_at") val updated_at: Date
)

data class CartResponse(
//    @SerializedName("isSuccess") val isSuccess: Boolean,
//    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: CartItem
)
data class CartItem(
    @SerializedName("name") val name: String,
    @SerializedName("en_price") val enPrice: Int,
    @SerializedName("won_price") val wonPrice: Int,
    @SerializedName("imgae") val imageUrl: String,
    @SerializedName("count") val count: Int,
    )

data class WishBoardResponse(
//    @SerializedName("isSuccess") val isSuccess: Boolean,
//    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: WishBoardItemResult
)

data class WishBoardItemResult(
    @SerializedName("count") val count: Int,
    @SerializedName("folders") val folders: List<Wishboard>,  //해당 부분 명칭이 필요한가?
    @SerializedName("nextCursor") val nextCursor: Int,
    //시간 부분은 String로 바꿔야할수도 있음
//    @SerializedName("created_at") val created_at: Date,
//    @SerializedName("updated_at") val updated_at: Date
)

data class WishBoardNameResponse(
//    @SerializedName("isSuccess") val isSuccess: Boolean,
//    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("folder_Name") val folder_Name: String
)

data class WishBoardDeleteResponse(
//    @SerializedName("isSuccess") val isSuccess: Boolean,
//    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)

data class WishListCreateRequest(
    val product_ids: List<Int>,
    val folder_name: String
)

data class WishListAddRequest(
    val product_ids: List<Int>,
    val folder_id: List<Int>
)

data class WishListEditRequest(
    val folder_id: Int,
    val new_folder_name: String
)

data class WishBoardDelRequest(
    val folder_ids: List<Int>
)

data class WishBoardListDelRequest(
    val product_ids: List<Int>
)