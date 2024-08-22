package com.umc.umc_6th_wit_android.home

import com.umc.umc_6th_wit_android.data.remote.home.HomeBestFoodResult

interface BestFoodView {
    fun onGetBestFoodSuccess(code: String, result: HomeBestFoodResult)
    fun onGetBestFoodFailure(code: String, message: String)
}