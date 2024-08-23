package com.umc.umc_6th_wit_android.data.remote.home
import android.content.Context
import android.util.Log
import com.umc.umc_6th_wit_android.home.BestFoodView
import com.umc.umc_6th_wit_android.home.CategoryView
import com.umc.umc_6th_wit_android.home.SubHomeView
import com.umc.umc_6th_wit_android.home.PersonalView
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.network.TokenRetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeService(private val context: Context) {
    private lateinit var subHomeView: SubHomeView
    private lateinit var personalView: PersonalView
    private lateinit var bestFoodView: BestFoodView
    private lateinit var categoryView: CategoryView

    //    private lateinit var searchMainView: SearchMainView
    //tokenManager 미리 설정
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val tokenManager = TokenManager(sharedPreferences)
    fun setSubHomeView(subHomeView: SubHomeView) {
        this.subHomeView = subHomeView
    }
    fun setPersonalView(personalView: PersonalView) {
        this.personalView = personalView
    }
    fun setBestFoodView(bestFoodView: BestFoodView) {
        this.bestFoodView = bestFoodView
    }
    fun setCategoryView(categoryView: CategoryView) {
        this.categoryView = categoryView
    }

    //홈 불러오기
    fun getHomeProducts() {
        // TokenManager에서 저장된 액세스 토큰을 가져옴
        val accessToken = tokenManager.getAccessToken()
        Log.d("HomeAccess", "$accessToken")

        if (accessToken != null) {

            val retrofit = TokenRetrofitManager(context)
            val homeService = retrofit.create(HomeRetrofitInterface::class.java)

            homeService.getHomeProducts("Bearer $accessToken").enqueue(object : Callback<HomeResponse> {
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

    //맞춤 추천템 불러오기
    fun getRecommendProducts() {
        // TokenManager에서 저장된 액세스 토큰을 가져옴
        val accessToken = tokenManager.getAccessToken()
        if (accessToken != null) {

            val retrofit = TokenRetrofitManager(context)
            val homeService = retrofit.create(HomeRetrofitInterface::class.java)

            homeService.getRecommendProduts("Bearer $accessToken").enqueue(object : Callback<HomePersonalResponse> {
                override fun onResponse(call: Call<HomePersonalResponse>, response: Response<HomePersonalResponse>) {
                    if (response.isSuccessful && response.code() == 200) {
                        val homePersonalResponse: HomePersonalResponse = response.body()!!

                        Log.d("HOME-RESPONSE", homePersonalResponse.toString())

                        when (val code = homePersonalResponse.code) {
                            "HOME_SUCCESS200" -> {
                                personalView.onGetPersonalSuccess(code, homePersonalResponse.result!!)
                            }
                            //MEMBER401
                            else -> personalView.onGetPersonalFailure(code, homePersonalResponse.message)
                        }
                    }
                }

                override fun onFailure(call: Call<HomePersonalResponse>, t: Throwable) {
                    personalView.onGetPersonalFailure("HOME_FAILURE400", "${t.message}")
                }
            })

        }
    }

    //맛도리 추천템 불러오기
    fun getBestFood() {
        // TokenManager에서 저장된 액세스 토큰을 가져옴
        val accessToken = tokenManager.getAccessToken()
        if (accessToken != null) {

            val retrofit = TokenRetrofitManager(context)
            val homeService = retrofit.create(HomeRetrofitInterface::class.java)

            homeService.getBestFood("Bearer $accessToken").enqueue(object : Callback<HomeBestFoodResponse> {
                override fun onResponse(call: Call<HomeBestFoodResponse>, response: Response<HomeBestFoodResponse>) {
                    if (response.isSuccessful && response.code() == 200) {
                        val homeBestFoodResponse: HomeBestFoodResponse = response.body()!!

                        Log.d("HOME-RESPONSE", homeBestFoodResponse.toString())

                        when (val code = homeBestFoodResponse.code) {
                            "HOME_SUCCESS200" -> {
                                bestFoodView.onGetBestFoodSuccess(code, homeBestFoodResponse.result!!)
                            }
                            //MEMBER401
                            else -> bestFoodView.onGetBestFoodFailure(code, homeBestFoodResponse.message)
                        }
                    }
                }

                override fun onFailure(call: Call<HomeBestFoodResponse>, t: Throwable) {
                    bestFoodView.onGetBestFoodFailure("HOME_FAILURE400", "${t.message}")
                }
            })

        }
    }

    //랭킹 category 불러오기 count=20 서버에서 고정
    fun getCategory(category: Int?, cursor: Int?) {
        // TokenManager에서 저장된 액세스 토큰을 가져옴
        val accessToken = tokenManager.getAccessToken()
        Log.d("CATEGORYINDEX3",category.toString() + cursor.toString())
        if (accessToken != null) {

            val retrofit = TokenRetrofitManager(context)
            val homeService = retrofit.create(HomeRetrofitInterface::class.java)

            homeService.getCategory("Bearer $accessToken", category, cursor).enqueue(object : Callback<CategoryResponse> {
                override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                    if (response.isSuccessful && response.code() == 200) {
                        val categoryResponse: CategoryResponse = response.body()!!

                        Log.d("CATEGORY-RESPONSE", categoryResponse.toString())

                        when (val code = categoryResponse.code) {
                            "HOME_SUCCESS200" -> {
                                categoryView.onGetCategorySuccess(code, categoryResponse.result!!)
                            }
                            //MEMBER401
                            else -> categoryView.onGetCategoryFailure(code, categoryResponse.message)
                        }
                    }
                }

                override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                    categoryView.onGetCategoryFailure("CATEGORY_FAILURE400", "${t.message}")
                }
            })

        }
    }

}