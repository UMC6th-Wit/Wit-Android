package com.umc.umc_6th_wit_android.data.remote.home

import com.umc.umc_6th_wit_android.data.remote.search.PopularSearchResponse
import com.umc.umc_6th_wit_android.data.remote.search.RecentDeleteResponse
import com.umc.umc_6th_wit_android.data.remote.search.RecentSearchResponse
import com.umc.umc_6th_wit_android.data.remote.search.SearchResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface HomeRetrofitInterface {
    @GET("home")
    fun getHomeProducts(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
//        @Query("count") count: Int
    ): Call<HomeResponse>
    
    //맞춤 추천템
    @GET("home/recommend")
    fun getRecommendProduts(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
    ) : Call<HomePersonalResponse>
    
    //맛도리 추천템
    @GET("home/nyam")
    fun getBestFood(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
    ) : Call<HomeBestFoodResponse>

    //랭킹 카테고리 불러오기
    /*categoryID : 0 전체 , 1 식품, 2 뷰티코스메틱, 3 의약품, 4 생활용품
      count : 갯수*/
    @GET("home/category")
    fun getCategory(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Query("category") category: Int?,
        @Query("cursor") cursor: Int?
    ) : Call<CategoryResponse>


}