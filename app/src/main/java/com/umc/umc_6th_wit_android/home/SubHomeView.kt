package com.umc.umc_6th_wit_android.home

import com.umc.umc_6th_wit_android.data.remote.home.HomeResult

interface SubHomeView {
    fun onGetHomeSuccess(code: String, result: HomeResult)
    fun onGetHomeFailure(code: String, message: String)
}