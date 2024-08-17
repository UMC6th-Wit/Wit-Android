import com.umc.umc_6th_wit_android.login.RefreshTokenApi
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
        if (accessToken != null) {
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        }

        // 2. 요청을 수행
        var response = chain.proceed(request)

        // 3. 토큰 만료 시 재발급
        if (response.code == 401) { // Unauthorized, 토큰 만료
            val refreshToken = tokenManager.getRefreshToken()

            if (refreshToken != null) {
                // 리프레시 토큰을 사용해 새로운 액세스 토큰을 발급받음
                val refreshCall = refreshTokenApi.refreshToken(refreshToken)
                val refreshResponse = refreshCall.execute()

                if (refreshResponse.isSuccessful) {
                    val refreshBody = refreshResponse.body()
                    val newAccessToken = refreshBody?.refreshToken

                    if (newAccessToken != null) {
                        // 새로운 액세스 토큰을 저장
                        tokenManager.saveAccessToken(newAccessToken)

                        // 4. 새로운 토큰을 사용하여 원래 요청을 다시 시도
                        request = request.newBuilder()
                            .removeHeader("Authorization")
                            .addHeader("Authorization", "Bearer $newAccessToken")
                            .build()

                        response = chain.proceed(request) // 원래 요청 재시도
                    }
                } else {
                    // Refresh token API 호출이 실패한 경우 처리
                    // 예를 들어, 로그아웃 처리 등을 할 수 있습니다.
                }
            }
        }

        return response
    }
}
