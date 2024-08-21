package com.umc.umc_6th_wit_android.search

import com.umc.umc_6th_wit_android.data.remote.search.SearchResult

interface SearchRsltView {
    fun onGetSearchSuccess(code: String, result: SearchResult)
    fun onGetSearchFailure(code: String, message: String)
}