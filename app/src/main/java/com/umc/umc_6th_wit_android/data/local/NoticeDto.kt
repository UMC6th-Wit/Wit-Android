package com.umc.umc_6th_wit_android.data.local

import java.io.Serializable

class NoticeDto(var title: String, var date: String):
    Serializable {
    override fun toString() = "$title - $date"
}