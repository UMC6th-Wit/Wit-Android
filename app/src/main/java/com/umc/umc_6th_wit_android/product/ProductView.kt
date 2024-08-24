package com.umc.umc_6th_wit_android.product

import com.umc.umc_6th_wit_android.data.remote.product.ProductResult
import com.umc.umc_6th_wit_android.wish.CartItem
import com.umc.umc_6th_wit_android.wish.WishBoardItemResult

interface ProductView {

    //상품 상세 정보 가져오기
    fun onGetProductSuccess(code: String, result: ProductResult)
    fun onGetProductFailure(code: String, message: String)

    //위시리스트 폴더 목록 조회
    fun onGetWishBoardListSuccess(code: String, result: WishBoardItemResult)
    fun onGetWishBoardListFailure(code: String, message: String)

    //위시리스트 폴더 상품 담기
    fun onPostWishtoBoardSuccess(code: String, result: WishBoardItemResult)
    fun onPostWishtoBoardFailure(code: String, message: String)


    //위시리스트 장바구니 담기
    fun onPostAddCartSuccess(code: String, response: CartItem)
    fun onPostAddCartFailure(code: String, error: String)

    //위시리스트 장바구니 빼기
    fun onPostDelCartSuccess(code: String, message: String)
    fun onPostDelCartFailure(code: String, message: String)

}