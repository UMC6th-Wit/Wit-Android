package com.umc.umc_6th_wit_android.data.remote.notice

import retrofit2.Call
import retrofit2.http.GET

interface NoticeRetrofitInterface {
    @GET("notices")
    fun getNotices(): Call<NoticeResponse>
}