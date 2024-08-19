package com.umc.umc_6th_wit_android.wish

interface WishView {

    //위시리스트 장바구니 목록 조회
    fun onGetWishListSuccess(code: String, result: WishItemResult)
    fun onGetWishListFailure(code: String, message: String)

    //위시리스트 폴더 목록 조회
    fun onGetWishBoardListSuccess(code: String, result: WishBoardItemResult)
    fun onGetWishBoardListFailure(code: String, message: String)

    //위시리스트 폴더 상품 담기
    fun onPostWishtoBoardSuccess(code: String, result: WishBoardItemResult)
    fun onPostWishtoBoardFailure(code: String, message: String)

    //위시리스트 폴더 이름 변경
    fun onPostWishListReNameSuccess(code: String, message: String)
    fun onPostWishListReNameFailure(code: String, message: String)

    //위시리스트 폴더 삭제
    fun onDeleteWishBoardListSuccess(code: String, message: String)
    fun onDeleteWishBoardListFailure(code: String, message: String)
}