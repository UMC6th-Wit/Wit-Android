package com.umc.umc_6th_wit_android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.umc.umc_6th_wit_android.login.LoginActivity
import com.umc.umc_6th_wit_android.login.TokenManager
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {  // Activity로 변경

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MyApp_Splash)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash) // 스플래시 화면 레이아웃 설정

        // SharedPreferences에서 TokenManager 초기화
        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        tokenManager = TokenManager(sharedPreferences)

        // 3초 동안 스플래시 화면 유지 후 화면 전환
        GlobalScope.launch {
            delay(3000) // 3000ms = 3초
            withContext(Dispatchers.Main) {
                checkTokenAndNavigate()
            }
        }
    }

    private fun checkTokenAndNavigate() {
        val refreshToken = tokenManager.getRefreshToken()
        if (refreshToken != null && isTokenValid(refreshToken)) {
            navigateToMainActivity()
        } else {
            navigateToLoginActivity()
        }
    }

    private fun isTokenValid(refreshToken: String): Boolean {
        // 실제 유효성 검사 로직 추가
        return true
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
