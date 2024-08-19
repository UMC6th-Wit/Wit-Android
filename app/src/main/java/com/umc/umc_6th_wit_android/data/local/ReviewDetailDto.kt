package com.umc.umc_6th_wit_android.data.local

import java.io.Serializable

class ReviewDetailDto (var imageResId: Int, var user: Int, var name: String, var date: Long, var rating: Double, var content: String, var isHelp : Boolean, var numHelp : Int) :

    Serializable {
        override fun toString() = "$user : $name / $imageResId / $date / $rating / $content / $isHelp / $numHelp"
}