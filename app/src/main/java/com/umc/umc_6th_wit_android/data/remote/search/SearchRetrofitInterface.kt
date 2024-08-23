package com.umc.umc_6th_wit_android.data.remote.search

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchRetrofitInterface {

    @GET("searches")
    fun getSearches(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Query("query") query: String? = " ",
        @Query("cursor") cursor: Int?,
        @Query("limit") limit: Int?
    ): Call<SearchResponse>

    @GET("user/recent-searches")
    fun getRecentSearches(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
    ) : Call<RecentSearchResponse>

    @DELETE("user/recent-searches")
    fun deleteRecentSearch(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Query("keyword") keyWord : String?
    ): Call<RecentDeleteResponse>

    @GET("searches/popular")
    fun getPopularSearches() : Call<PopularSearchResponse>
}