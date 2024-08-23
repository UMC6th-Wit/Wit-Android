package com.umc.umc_6th_wit_android.home

import com.umc.umc_6th_wit_android.data.remote.home.CategoryResult

interface CategoryView {
    fun onGetCategorySuccess(code: String, result: CategoryResult)
    fun onGetCategoryFailure(code: String, message: String)
}