package com.umc.umc_6th_wit_android.home

import com.umc.umc_6th_wit_android.data.remote.home.HomePersonalResult

interface PersonalView {
    fun onGetPersonalSuccess(code: String, result: HomePersonalResult)
    fun onGetPersonalFailure(code: String, message: String)
}