package com.umc.umc_6th_wit_android.wish

interface WishListView {

    //위시리스트 폴더 상세 조회
    fun onGetWishBoardSuccess(code: String, result: WishItemResult)
    fun onGetWishBoardFailure(code: String, message: String)

    //위시리스트 폴더 목록 조회
    fun onGetWishBoardListSuccess(code: String, result: WishBoardItemResult)
    fun onGetWishBoardListFailure(code: String, message: String)

    //위시리스트 폴더 상품 담기
    fun onPostWishtoBoardSuccess(code: String, result: WishBoardItemResult)
    fun onPostWishtoBoardFailure(code: String, message: String)

    //위시리스트 폴더 목록 삭제
    fun onDeleteToWishBoardSuccess(code: String, message: String)
    fun onDeleteToWishBoardFailure(code: String, message: String)
}