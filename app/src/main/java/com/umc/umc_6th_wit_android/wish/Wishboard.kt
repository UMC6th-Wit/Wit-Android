package com.umc.umc_6th_wit_android.wish

data class Wishboard(
    val subcategory_Id: Int,
    val subcategory_Name: String,
    var folder_Id: Int,
    var folder_Name: String,
    val products : List<WishItem>
)
