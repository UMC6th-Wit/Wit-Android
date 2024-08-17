package com.umc.umc_6th_wit_android.mypage

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

// Retrofit을 통해 호출될 API를 정의하는 인터페이스
interface UserService {
    @GET("user/{user_id}")
    fun getUserInfo(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Path("user_id") userId: String  // URL 경로의 user_id를 전달
    ): Call<UserInfoResponse>
}

data class UserInfoResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: UserResult?
)

data class UserResult(
    val user_id: String,
    val username: String,
    val usernickname: String,
    val gender: String,
    val social_login: String,
    val age: Int,
    val birth: String,
    val created_at: String
)

