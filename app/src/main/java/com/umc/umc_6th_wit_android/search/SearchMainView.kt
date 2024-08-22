package com.umc.umc_6th_wit_android.search
interface SearchMainView {
    fun onGetRecentSearchSuccess(code: String, result: List<String>)
    fun onGetRecentSearchFailure(code: String, message: String)
    fun onDeleteRecentSearchSuccess(code: String, message: String)
    fun onDeleteRecentSearchFailure(code: String, message: String)
    fun onGetPopularSearchSuccess(code: String, result: List<String>)
    fun onGetPopularSearchFailure(code: String, message: String)
}