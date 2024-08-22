package com.umc.umc_6th_wit_android.data.remote.home
import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.umc.umc_6th_wit_android.home.SubHomeView
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.network.TokenRetrofitManager
import com.umc.umc_6th_wit_android.search.SearchMainView
import com.umc.umc_6th_wit_android.search.SearchRsltView
import com.umc.umc_6th_wit_android.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeService(private val context: Context) {
    private lateinit var subHomeView: SubHomeView
//    private lateinit var searchMainView: SearchMainView
    //tokenManager 미리 설정
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val tokenManager = TokenManager(sharedPreferences)
    fun setSubHomeView(subHomeView: SubHomeView) {
        this.subHomeView = subHomeView
    }


    //검색결과
    fun getHomeProducts() {
        // TokenManager에서 저장된 액세스 토큰을 가져옴
        val accessToken = tokenManager.getAccessToken()
        if (accessToken != null) {

            val retrofit = TokenRetrofitManager(context)
            val homeService = retrofit.create(HomeRetrofitInterface::class.java)

            homeService.getHomeProducts("Bearer $accessToken", 3).enqueue(object : Callback<HomeResponse> {
                override fun onResponse(call: Call<HomeResponse>, response: Response<HomeResponse>) {
                    if (response.isSuccessful && response.code() == 200) {
                        val searchResponse: HomeResponse = response.body()!!

                        Log.d("HOME-RESPONSE", searchResponse.toString())

                        when (val code = searchResponse.code) {
                            "HOME_SUCCESS200" -> {
                                subHomeView.onGetHomeSuccess(code, searchResponse.result!!)
                            }
                            //MEMBER401
                            else -> subHomeView.onGetHomeFailure(code, searchResponse.message)
                        }
                    }
                }

                override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                    subHomeView.onGetHomeFailure("HOME_FAILURE400", "${t.message}")
                }
            })

        }
    }

}