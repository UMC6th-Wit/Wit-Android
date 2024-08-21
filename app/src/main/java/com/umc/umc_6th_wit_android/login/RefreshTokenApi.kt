package com.umc.umc_6th_wit_android.login

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshTokenApi {
    @POST("user/refresh_token")
    //AccesToken 갱신
    fun refreshToken(@Body request: RefreshTokenRequest): Call<RefreshTokenResponse>
}

data class RefreshTokenRequest(
    val refreshToken: String
)

data class RefreshTokenResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String  // 새롭게 발급된 액세스 토큰을 포함하는 필드
)

