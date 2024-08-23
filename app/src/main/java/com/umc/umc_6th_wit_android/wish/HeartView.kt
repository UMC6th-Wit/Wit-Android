package com.umc.umc_6th_wit_android.wish

interface HeartView {

    //위시리스트 폴더 상세 조회
    fun onAddWishSuccess(code: String, result: String)
    fun onAddWishFailure(code: String, message: String)
    fun onDeleteWishSuccess(code: String, result: String)
    fun onDeleteWishFailure(code: String, message: String)

}