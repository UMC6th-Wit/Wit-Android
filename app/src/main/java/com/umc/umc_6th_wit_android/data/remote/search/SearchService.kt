package com.umc.umc_6th_wit_android.data.remote.search
import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.network.TokenRetrofitManager
import com.umc.umc_6th_wit_android.search.SearchMainView
import com.umc.umc_6th_wit_android.search.SearchRsltView
import com.umc.umc_6th_wit_android.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchService(private val context: Context) {
    private lateinit var searchRsltView: SearchRsltView
    private lateinit var searchMainView: SearchMainView
    //tokenManager 미리 설정
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val tokenManager = TokenManager(sharedPreferences)
    fun setSearchRsltView(searchRsltView: SearchRsltView) {
        this.searchRsltView = searchRsltView
    }
    fun setSearchMainView(searchMainView: SearchMainView) {
        this.searchMainView = searchMainView
    }

    //검색결과
    fun getSearches(query : String?) {
        // TokenManager에서 저장된 액세스 토큰을 가져옴
        val accessToken = tokenManager.getAccessToken()
        if (accessToken != null) {

            val retrofit = TokenRetrofitManager(context)
            val searchService = retrofit.create(SearchRetrofitInterface::class.java)

            searchService.getSearches("Bearer $accessToken", query).enqueue(object : Callback<SearchResponse> {
                override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                    if (response.isSuccessful && response.code() == 200) {
                        val searchResponse: SearchResponse = response.body()!!

                        Log.d("SEARCH-RESPONSE", searchResponse.toString())

                        when (val code = searchResponse.code) {
                            "SEARCHES200" -> {
                                searchRsltView.onGetSearchSuccess(code, searchResponse.result!!)
                            }
                            //MEMBER401
                            else -> searchRsltView.onGetSearchFailure(code, searchResponse.message)
                        }
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    searchRsltView.onGetSearchFailure("SEARCHES400", "검색 실패.")
                }
            })

        }
    }

    //개인 최근 검색어 recent도 불러오기
    fun getRecentSearches(){
        // TokenManager에서 저장된 액세스 토큰을 가져옴
        val accessToken = tokenManager.getAccessToken()

        if (accessToken != null) {

            val retrofit = TokenRetrofitManager(context)
            val searchService = retrofit.create(SearchRetrofitInterface::class.java)

            searchService.getRecentSearches("Bearer $accessToken")
                .enqueue(object : Callback<RecentSearchResponse> {
                    override fun onResponse(
                        call: Call<RecentSearchResponse>,
                        response: Response<RecentSearchResponse>
                    ) {
                        if (response.isSuccessful && response.code() == 200) {
                            val recentSearchResponse: RecentSearchResponse = response.body()!!

                            Log.d("SEARCH-RESPONSE", recentSearchResponse.toString())

                            when (val code = recentSearchResponse.code) {
                                "RECENT_SEARCHES200" -> {
                                    searchMainView.onGetRecentSearchSuccess(
                                        code,
                                        recentSearchResponse.result
                                    )
                                }

                                else -> searchMainView.onGetRecentSearchFailure(
                                    code,
                                    recentSearchResponse.message
                                )
                            }
                        }
                    }

                    override fun onFailure(call: Call<RecentSearchResponse>, t: Throwable) {
                        searchMainView.onGetRecentSearchFailure(
                            "RECENT_SEARCHES400",
                            "${t.message}"
                        )
                    }
                })
        }
    }

    //최근 검색어 삭제
    fun deleteRecentSearches(keyWord : String?){
        Log.d("keywordService", keyWord!!)

        // TokenManager에서 저장된 액세스 토큰을 가져옴
        val accessToken = tokenManager.getAccessToken()

        if (accessToken != null) {

            val retrofit = TokenRetrofitManager(context)
            val searchService = retrofit.create(SearchRetrofitInterface::class.java)

            searchService.deleteRecentSearch("Bearer $accessToken", keyWord!!)
                .enqueue(object : Callback<RecentDeleteResponse> {
                    override fun onResponse(call: Call<RecentDeleteResponse>, response: Response<RecentDeleteResponse>) {
                        if (response.isSuccessful && response.code() == 200) {
                            val recentDeleteResponse: RecentDeleteResponse = response.body()!!

                            Log.d("SEARCH-RESPONSE", recentDeleteResponse.toString())

                            when (val code = recentDeleteResponse.code) {
                                "DELETE_ONE_RECENT_SEARCHES_SUCCESS" -> {
                                    searchMainView.onDeleteRecentSearchSuccess(code, recentDeleteResponse.message)
                                }
                                "RECENT_SEARCH_DELETE_ALL_SUCCESS200" -> {
                                    searchMainView.onDeleteRecentSearchSuccess(code, recentDeleteResponse.message)
                                }

                                else -> searchMainView.onDeleteRecentSearchFailure(code, recentDeleteResponse.message)
                            }
                        }
                    }

                    override fun onFailure(call: Call<RecentDeleteResponse>, t: Throwable) {
                        searchMainView.onDeleteRecentSearchFailure("RECENT_DELETE400","${t.message}")
                    }
                })
        }

    }

    //전체 인기 검색어 popular 요청
    fun getPopularSearches(){
        val searchService = getRetrofit().create(SearchRetrofitInterface::class.java)

        searchService.getPopularSearches().enqueue(object : Callback<PopularSearchResponse> {

            override fun onResponse(
                call: Call<PopularSearchResponse>,
                response: Response<PopularSearchResponse>
            ) {
                if (response.isSuccessful && response.code() == 200) {
                    val popularSearchResponse: PopularSearchResponse = response.body()!!

                    Log.d("SEARCH-RESPONSE", popularSearchResponse.toString())

                    when (val code = popularSearchResponse.code) {
                        "POPULAR_SEARCHES200" -> {
                            searchMainView.onGetPopularSearchSuccess(code, popularSearchResponse.result)
                        }

                        else -> searchMainView.onGetPopularSearchFailure(code, popularSearchResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<PopularSearchResponse>, t: Throwable) {
                searchMainView.onGetPopularSearchFailure("RECENT_SEARCHES400", "인기 검색어 조회에 실패했습니다.")
            }
        })
    }
}