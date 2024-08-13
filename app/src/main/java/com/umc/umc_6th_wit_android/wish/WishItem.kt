package com.umc.umc_6th_wit_android.wish

data class WishItem(
    val productId: Int,
    val name: String,
    val won_price: String,
    val en_price: String,
    val image_url: Int? = null,
    val description: String,
    val reviews : Int,
    val rating: Double
)
