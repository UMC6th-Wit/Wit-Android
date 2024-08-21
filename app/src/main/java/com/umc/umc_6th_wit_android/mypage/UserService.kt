package com.umc.umc_6th_wit_android.mypage

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

// Retrofit을 통해 호출될 API를 정의하는 인터페이스
interface UserService {
    @GET("/user")
    fun getUserInfo(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
    ): Call<UserInfoResponse>

    // 유저 정보 수정 (POST)
    @POST("/user")
    fun updateUserInfo(
        @Header("Authorization") accessToken: String,
        @Body userInfoUpdateRequest: UserInfoUpdateRequest
    ): Call<UserInfoResponse>

    @GET("user/logout")
    fun logout(
        @Header("Authorization") accessToken: String
    ): Call<LogoutResponse>

    @DELETE("user/withdraw")
    fun withdrawUser(
        @Header("Authorization") accessToken: String
    ): Call<WithdrawResponse>

    @GET("/user/profile_image")
    fun getUserProfileImage(
        @Header("Authorization") accessToken: String
    ): Call<UserProfileResponse>

}

data class UserInfoResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: UserResult?
)

data class LogoutResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String
)

data class WithdrawResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String
)


data class UserResult(
    val user_id: String,
    val username: String,
    val usernickname: String,
    val gender: String,
    val social_login: String,
    val age: Int,
    var birth: String,
    val created_at: String
)

data class UserInfoUpdateRequest(
    val username: String,
    val usernickname: String,
    val gender: String,
    val age: Int,     // 고정된 나이로 보내는 경우 (예: 30)
    val birth: String // 월-일 형식 (예: "02-22")
)

data class UserProfileResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?  // 이미지 URL이 담긴 필드
)



