package com.umc.umc_6th_wit_android.data.local

import java.io.Serializable

//임시 test data
data class CategoryDto (var image: Int, var address: String, var title: String, var yen: String, var won: String) :
    Serializable {
    override fun toString() = "$title : $address - $yen ($won)"
}