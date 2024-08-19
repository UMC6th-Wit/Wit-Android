package com.umc.umc_6th_wit_android.wish

interface FolderView {
    //위시리스트 폴더 생성
    fun onPostWishListCreateSuccess(code: String, result: WishBoardItemResult)
    fun onPostWishListCreateFailure(code: String, message: String)
}