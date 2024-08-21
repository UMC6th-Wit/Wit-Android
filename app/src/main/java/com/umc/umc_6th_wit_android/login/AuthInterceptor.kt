import android.util.Log
import com.google.gson.Gson
import com.umc.umc_6th_wit_android.login.RefreshTokenApi
import com.umc.umc_6th_wit_android.login.RefreshTokenRequest
import com.umc.umc_6th_wit_android.login.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response as RetrofitResponse

class AuthInterceptor(
    private val tokenManager: TokenManager,
    private val refreshTokenApi: RefreshTokenApi
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // 1. 현재 액세스 토큰을 헤더에 추가
        val accessToken = tokenManager.getAccessToken()
        Log.d("AuthInterceptor","accessToken: $accessToken")
        if (accessToken != null) {
            request = request.newBuilder()
                .addHeader("AccessToken", "$accessToken")
                .build()
        }

        // 2. 요청을 수행
        var response = chain.proceed(request)
        Log.d("AuthInterceptor", "response.isSuccessful: ${response.isSuccessful}")


        // 3. HTTP 상태 코드가 200이어도 서버 응답 본문을 확인
        if (response.isSuccessful) {
            val responseBody = response.peekBody(Long.MAX_VALUE).string()

            // 4. 응답 본문에서 토큰 만료 여부 확인
            val serverResponse = parseServerResponse(responseBody)
            Log.d("AuthInterceptor", "serverResponse.code: ${serverResponse.code}")
            if (serverResponse.code == "TOKEN401") {
                // 토큰이 만료된 경우, 재발급 시도
                Log.d("AuthInterceptor", "토큰 만료, 재발급 시도")
                val refreshToken = tokenManager.getRefreshToken()
                Log.d("AuthInterceptor","refreshToken: $refreshToken")

                if (refreshToken != null) {
                    val refreshCall = refreshTokenApi.refreshToken(RefreshTokenRequest(refreshToken))
                    val refreshResponse = refreshCall.execute()
                    Log.d("AuthInterceptor", "refreshResponse.isSuccessful: ${refreshResponse.isSuccessful}")

                    if (refreshResponse.isSuccessful) {
                        val refreshBody = refreshResponse.body()
                        val newAccessToken = refreshBody?.result

                        if (newAccessToken != null) {
                            // 새로운 액세스 토큰을 저장
                            tokenManager.saveAccessToken(newAccessToken)
                            Log.d("AuthInterceptor", "new accessToken: $newAccessToken")

                            // 5. 새로운 토큰을 사용하여 원래 요청을 다시 시도
                            request = request.newBuilder()
                                .removeHeader("Authorization")
                                .addHeader("AccessToken", "$newAccessToken")
                                .build()

                            response = chain.proceed(request) // 원래 요청 재시도
                        }
                    } else {
                        // Refresh token API 호출이 실패한 경우 처리 (로그아웃 등)
                        Log.d("AuthInterceptor", "refresh token 호출 실패")
                    }
                }
            }
        }

        return response
    }

    // 서버 응답 파싱 함수
    private fun parseServerResponse(responseBody: String): ServerResponse {
        return Gson().fromJson(responseBody, ServerResponse::class.java)
    }

    // 서버 응답 데이터 클래스
    data class ServerResponse(
        val isSuccess: Boolean,
        val code: String,
        val message: String
    )
}
