package com.umc.umc_6th_wit_android.wish

data class Wishboard(
    var id: Int,
    val title: String,
    val quantity: Int,
    val images: List<Int>
)
