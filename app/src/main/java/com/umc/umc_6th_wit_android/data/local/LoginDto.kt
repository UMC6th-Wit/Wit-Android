package com.umc.umc_6th_wit_android.data.local

data class LoginDto(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?  // 네이버 로그인 페이지 내용
)
