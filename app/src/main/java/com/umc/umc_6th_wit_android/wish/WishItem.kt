package com.umc.umc_6th_wit_android.wish

data class WishItem(
    val id: Int,
    val image: Int? = null,
    val title: String,
    val jpy: String,
    val krw: String,
    val rating: Double,
    val nop : Int
)
