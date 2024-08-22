package com.umc.umc_6th_wit_android.login

import com.umc.umc_6th_wit_android.data.local.LoginDto
import retrofit2.Call
import retrofit2.http.GET

interface LoginApi {
    @GET("/v3/your-api-endpoint")
    fun getNaverLoginPage(): Call<LoginDto>
}