package com.umc.umc_6th_wit_android.data.remote.home
import android.content.Context
import android.util.Log
import com.navercorp.nid.oauth.NidOAuthPreferencesManager.accessToken
import com.umc.umc_6th_wit_android.NetworkModule
import com.umc.umc_6th_wit_android.home.BestFoodView
import com.umc.umc_6th_wit_android.home.CategoryView
import com.umc.umc_6th_wit_android.home.SubHomeView
import com.umc.umc_6th_wit_android.home.PersonalView
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.network.TokenRetrofitManager
import com.umc.umc_6th_wit_android.wish.CartResponse
import com.umc.umc_6th_wit_android.wish.HeartView
import com.umc.umc_6th_wit_android.wish.WishBoardDeleteResponse
import com.umc.umc_6th_wit_android.wish.WishBoardListDelRequest
import com.umc.umc_6th_wit_android.wish.WishRetrofitInterfaces
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeService(private val context: Context) {
    private lateinit var subHomeView: SubHomeView
    private lateinit var personalView: PersonalView
    private lateinit var bestFoodView: BestFoodView
    private lateinit var categoryView: CategoryView
    private lateinit var heartView: HeartView

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
    fun setHeartView(heartView: HeartView){
        this.heartView = heartView
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


    //장바구니에 추가
    fun addCart(productId: Int){
        Log.d("ADDCART3",productId.toString())

        val accessToken = tokenManager.getAccessToken()
        Log.d("ADDCART3",productId.toString() + accessToken)

        if (accessToken != null) {
            val retrofit = TokenRetrofitManager(context)
            val homeService = retrofit.create(WishRetrofitInterfaces::class.java)

            homeService.addCart("Bearer $accessToken", productId).enqueue(object : Callback<CartResponse> {
                override fun onResponse(call: Call<CartResponse>, response: Response<CartResponse>) {
                    if (response.isSuccessful && response.code() == 200) {
                        val cartResponse: CartResponse = response.body()!!

                        Log.d("CART201-RESPONSE", cartResponse.toString())

                        when (val message = cartResponse.message) {
                            "제품이 장바구니에 성공적으로 추가되었습니다." -> {
                                heartView.onAddWishSuccess(message, cartResponse.message)
                            }
                            else -> {
                                heartView.onAddWishFailure(message, cartResponse.message)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<CartResponse>, t: Throwable) {
                    // 실패 처리
                    Log.d("CART201-ERROR", t.message.toString())
                }
            })
        }

    }

    //장바구니에서 제거
    fun delCart(productId: Int) {
        val product_id: List<Int> = listOf(productId)
        val accessToken = tokenManager.getAccessToken()

        if (accessToken != null) {
            val retrofit = TokenRetrofitManager(context)
            val homeService = retrofit.create(WishRetrofitInterfaces::class.java)

            homeService.delCart("Bearer $accessToken", WishBoardListDelRequest(product_id))
                .enqueue(object : Callback<WishBoardDeleteResponse> {
                    override fun onResponse(call: Call<WishBoardDeleteResponse>, response: Response<WishBoardDeleteResponse>) {
                        if (response.isSuccessful && response.code() == 200) {
                            val wishBoardListResponse: WishBoardDeleteResponse = response.body()!!

                            Log.d("CART301-RESPONSE", wishBoardListResponse.toString())

                            when (val message = wishBoardListResponse.message) {
                                "제품이 장바구니에서 성공적으로 제거되었습니다." -> {
                                    heartView.onDeleteWishSuccess(message, wishBoardListResponse.message)
                                }
                                else -> {
                                    heartView.onDeleteWishFailure(message, wishBoardListResponse.message)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<WishBoardDeleteResponse>, t: Throwable) {
                        // 실패 처리
                        Log.d("CART301-ERROR", t.message.toString())
                    }
                })
        }
    }
}