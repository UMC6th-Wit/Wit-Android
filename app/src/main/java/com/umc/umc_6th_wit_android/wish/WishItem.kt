package com.umc.umc_6th_wit_android.wish

import java.io.Serializable


data class WishItem(
    val product_id: Int,
    val name: String,
    val en_price: Int,
    val won_price: Int,
    val image: String? = null,
    val average_rating: Double,
    val review_count : Int,
    val heart: Boolean
) : Serializable
