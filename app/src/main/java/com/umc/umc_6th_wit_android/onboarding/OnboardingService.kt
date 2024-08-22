package com.umc.umc_6th_wit_android.onboarding

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OnboardingService {

    @POST("/onboarding")
    fun sendOnboardingData(
        @Header("Authorization") accessToken: String,
        @Body onboardingRequest: OnboardingRequest
    ): Call<OnboardingResponse>  // 서버에서 온 응답을 처리할 클래스
}

data class OnboardingRequest(
    val souvenirname: List<String>,  // 기념품 이름 리스트
    val destination: List<String>,  // 여행 지역 리스트
    val personalityname: List<String>  // 여행 성향 리스트
)

data class OnboardingResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Any?  // 응답 데이터가 특정 형식일 경우 해당 형식으로 변경
)

