package com.umc.umc_6th_wit_android.data.local

import java.io.Serializable

class PopupDto (var title: String, var time: Int):
    Serializable {
    override fun toString() = "$title - $time"
}
