package com.umc.umc_6th_wit_android.login

import android.content.SharedPreferences

class TokenManager(private val sharedPreferences: SharedPreferences) {

    companion object {
        private const val ACCESS_TOKEN_KEY = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN_KEY = "REFRESH_TOKEN"
        private const val USER_ID_KEY = "USER_ID"
    }

    // Access Token 관리
    fun saveAccessToken(token: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN_KEY, token).apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(ACCESS_TOKEN_KEY, null)
    }

    // Refresh Token 관리
    fun saveRefreshToken(token: String) {
        sharedPreferences.edit().putString(REFRESH_TOKEN_KEY, token).apply()
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(REFRESH_TOKEN_KEY, null)
    }

    // User ID 관리
    fun saveUserId(userId: String) {
        sharedPreferences.edit().putString(USER_ID_KEY, userId).apply()
    }

    fun getUserId(): String? {
        return sharedPreferences.getString(USER_ID_KEY, null)
    }

    // Clear all tokens and user info
    fun clearTokens() {
        sharedPreferences.edit().clear().apply()
    }
}
