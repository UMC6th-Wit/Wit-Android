package com.umc.umc_6th_wit_android.wish

data class Wishboard(
    var folder_id: Int,
    var folder_name: String,
    val images : List<String>
)
