package com.umc.umc_6th_wit_android.login

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenApi {
    @POST("user/refresh_token")
    fun refreshToken(@Body refreshToken: String): Call<RefreshTokenResponse>
}

data class RefreshTokenResponse(
    val refreshToken: String
)


