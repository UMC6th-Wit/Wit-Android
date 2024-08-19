package com.umc.umc_6th_wit_android.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.databinding.ActivityKakaoBinding

class KakaoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKakaoBinding
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TokenManager 초기화
        tokenManager = TokenManager(getSharedPreferences("auth_prefs", Context.MODE_PRIVATE))
        binding = ActivityKakaoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initWebView()
    }

    private fun initWebView() {
        binding.kakaoLoginWebView.apply {
            settings.javaScriptEnabled = true  // JavaScript 활성화

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    if (url != null && url.contains("/api/users/kakao_signin/callback")) {
                        // 잘못된 콜백 URL이 호출되었을 때 처리
                        val correctUrl = url.replace(
                            "/api/users/kakao_signin/callback",
                            "/user/kakao_signin/callback"
                        )
                        view?.loadUrl(correctUrl)
                        Log.d("KakaoActivity", "correctUrl: $correctUrl")
                        return true  // WebView에서 이 URL을 로드하지 않음
                    }
                    return false  // 다른 URL은 WebView에서 로드
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.d("KakaoActivity", "Page finished loading: $url")

                    // 특정 콜백 URL에서 HTML을 가져와 처리
                    if (url != null && url.contains("/user/kakao_signin/callback")) {
                        view?.evaluateJavascript(
                            "(function() { return document.documentElement.outerHTML; })();"
                        ) { html ->
                            // HTML을 가져온 후 파싱하여 필요한 데이터 추출
                            parseHtmlForTokens(html)
                        }
                    }
                }
            }

            // 네이버 로그인 페이지 로드
            loadUrl("http://43.202.194.145/user/kakao_signin")
        }
    }

    private fun parseHtmlForTokens(html: String) {
        // 로그로 HTML 출력 (필요 시)
        Log.d("KakaoActivity", "HTML content: $html")

        // 1. 유니코드 이스케이프된 문자를 먼저 디코딩
        val decodedHtml = decodeUnicodeCharacters(html)

        // 2. HTML에서 <script> 태그 내의 JSON 데이터 추출
        val scriptTagRegex = "window\\.opener\\.postMessage\\((\\{.*\\}), '\\*'\\);".toRegex()
        val scriptContentMatch = scriptTagRegex.find(decodedHtml)

        if (scriptContentMatch != null) {
            val jsonData = scriptContentMatch.groupValues[1]

            // 3. JSON 데이터를 다시 이스케이프 문자 제거
            val cleanJsonData = jsonData.replace("\\\"", "\"")

            // 4. 디코딩한 JSON 데이터를 파싱
            try {
                val gson = Gson()
                val loginData = gson.fromJson(cleanJsonData, LoginData::class.java)

                // 5. 추출된 데이터를 로그로 출력
                Log.d("KakaoActivity", "User ID: ${loginData.result.user_id}")
                Log.d("KakaoActivity", "Access Token: ${loginData.result.accessToken}")
                Log.d("KakaoActivity", "Refresh Token: ${loginData.result.refreshToken}")

                // 6. 로그인 데이터를 TokenManager에 저장
                saveLoginDataToTokenManager(loginData)

                // 7. user_id가 null이 아니면 MainActivity로 이동
                if (loginData.result.user_id != null) {
                    navigateToMainActivity()
                }
            } catch (e: Exception) {
                Log.e("KakaoActivity", "Failed to parse JSON: ${e.message}")
            }
        } else {
            Log.e("KakaoActivity", "Failed to find JSON data in script tag")
        }
    }

    private fun saveLoginDataToTokenManager(loginData: LoginData) {
        // TokenManager에 로그인 데이터를 저장
        tokenManager.saveUserId(loginData.result.user_id.toString())
        tokenManager.saveAccessToken(loginData.result.accessToken)
        tokenManager.saveRefreshToken(loginData.result.refreshToken)
    }

    private fun navigateToMainActivity() {
        // MainActivity로 이동
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()  // KakaoActivity 종료
    }

    // 유니코드 이스케이프된 문자를 디코딩하는 함수
    private fun decodeUnicodeCharacters(input: String): String {
        return input.replace("\\\\u([0-9A-Fa-f]{4})".toRegex()) {
            val charCode = it.groupValues[1].toInt(16)
            charCode.toChar().toString()
        }
    }

    // JSON 데이터의 구조를 반영한 데이터 클래스
    data class LoginData(
        val isSuccess: Boolean,
        val code: String,
        val message: String,
        val result: Result
    ) {
        data class Result(
            val user_id: Int,
            val accessToken: String,
            val refreshToken: String
        )
    }
}
