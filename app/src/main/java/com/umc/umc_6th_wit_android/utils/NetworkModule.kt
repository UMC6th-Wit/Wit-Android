package com.umc.umc_6th_wit_android.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//가상 api mocky 주소
const val BASE_URL = "https://run.mocky.io"
fun getRetrofit(): Retrofit {
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    return retrofit
}