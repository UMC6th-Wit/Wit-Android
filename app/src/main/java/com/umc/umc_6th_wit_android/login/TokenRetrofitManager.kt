package com.umc.umc_6th_wit_android.network

import AuthInterceptor
import android.content.Context
import com.umc.umc_6th_wit_android.login.RefreshTokenApi
import com.umc.umc_6th_wit_android.login.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TokenRetrofitManager(context: Context) {

    private val BASE_URL = "http://43.202.194.145/"

    private val retrofit: Retrofit

    init {
        // TokenManager 초기화
        val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val tokenManager = TokenManager(sharedPreferences)

        // RefreshTokenApi 초기화
        val refreshTokenApi = createRefreshTokenApi()

        // OkHttpClient에 AuthInterceptor 추가
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(tokenManager, refreshTokenApi))
            .build()

        // Retrofit 인스턴스 생성
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // 외부에서 Retrofit 인스턴스에 접근할 수 있도록 메서드 제공
    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    // RefreshTokenApi를 초기화하는 함수
    private fun createRefreshTokenApi(): RefreshTokenApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RefreshTokenApi::class.java)
    }
}
