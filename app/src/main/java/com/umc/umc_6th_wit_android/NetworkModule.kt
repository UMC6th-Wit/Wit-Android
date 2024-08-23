package com.umc.umc_6th_wit_android

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 레트로핏 객체 생성
class NetworkModule {
    companion object {
        private const val BASE_URL = "http://43.202.194.145/"
//        private const val BASE_URL = "http://10.0.2.2:3000/"
        private var INSTANCE: Retrofit? = null

        fun getInstance(): Retrofit {
            if (INSTANCE == null) {
                INSTANCE = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // 서버에서 온 json 응답을 data class로 자동 변환
                    .build()
            }
            return INSTANCE!!
        }
    }
}

