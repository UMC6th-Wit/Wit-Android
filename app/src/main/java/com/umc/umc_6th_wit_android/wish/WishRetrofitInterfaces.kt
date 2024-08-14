package com.umc.umc_6th_wit_android.wish

import retrofit2.*
import retrofit2.http.*

interface WishRetrofitInterfaces {

    //위시리스트 장바구니 목록 조회
    @GET("wishlist")
    fun getWishList(): Call<WishResponse>

    //위시리스트 폴더 목록 조회 *삭제 예정
    @GET("wishboard")
    fun getWishBoardList(): Call<WishBoardResponse>

    //위시리스트 폴더 상세 조회
    @GET("wishlist/{folderId}")
    fun getWishBoard(@Path("folderId") folderId: Int): Call<WishBoardResponse>

    //위시리스트 폴더 상품 담기
    @POST("wishlist")
    fun postWishtoBoard(@Body wishboard: Wishboard, @Body wishitems: List<WishItem>): Call<WishBoardResponse>

    //위시리스트 폴더 생성
    @POST("wishlist/create")
    fun postWishListCreate(@Body folder_Name: String): Call<WishBoardResponse>

    //위시리스트 폴더 이름 변경
    @POST("wishlist/rename")
    fun postWishListReName(@Body folder_Name: String): Call<WishBoardNameResponse>

    //위시리스트 폴더 삭제 *request 부분 추가 예정
    @DELETE("wishlist")
    fun delWishBoardList(): Call<WishBoardDeleteResponse>

    //위시리스트 폴더 목록 삭제 *request 부분 추가 예정
    @DELETE("wishlist/{folderId}")
    fun deltoWishBoard(@Path("folderId") folderid: Int): Call<WishBoardDeleteResponse>
}