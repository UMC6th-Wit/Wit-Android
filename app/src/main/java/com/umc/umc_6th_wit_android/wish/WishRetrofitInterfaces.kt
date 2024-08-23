package com.umc.umc_6th_wit_android.wish

import retrofit2.*
import retrofit2.http.*

interface WishRetrofitInterfaces {

    //위시리스트 장바구니 목록 조회
    @GET("wishlist")
    fun getWishList(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Query("cursor") cursor: Int?,
        @Query("limit") limit: Int?
    ): Call<WishResponse>

    //제품 장바구니에 담기
    @POST("product/add-cart/{product_id}")
    fun addCart(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Path("product_id") productId: Int,
    ): Call<CartResponse>

    //제품 장바구니에 빼기
    @POST("product/delete-cart")
    fun delCart(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Body request: WishBoardListDelRequest
    ): Call<WishBoardDeleteResponse>

    //위시리스트 폴더 목록 조회
    @GET("wishlist/user-folders")
    fun getWishBoardList(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Query("cursor") cursor: Int?,
        @Query("limit") limit: Int?
    ): Call<WishBoardResponse>

    //위시리스트 폴더 상세 조회
    @GET("wishlist/folder-products/{folderId}")
    fun getWishBoard(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Path("folderId") folderId: Int,
        @Query("cursor") cursor: Int?,
        @Query("limit") limit: Int?
    ): Call<WishResponse>

    //위시리스트 폴더 생성
    @POST("wishlist/create-folder")
    fun postWishListCreate(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Body request: WishListCreateRequest
    ): Call<WishBoardResponse>

    //위시리스트 폴더 상품 담기
    @POST("wishlist/add-product")
    fun postWishtoBoard(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Body request: WishListAddRequest
    ): Call<WishBoardResponse>

    //위시리스트 폴더 이름 변경
    @POST("wishlist/update-folder")
    fun postWishListReName(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Body request: WishListEditRequest
    ): Call<WishBoardNameResponse>

    //위시리스트 폴더 삭제 *request 부분 추가 예정
    @POST("wishlist/delete-folders")
    fun delWishBoardList(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Body request: WishBoardDelRequest
    ): Call<WishBoardDeleteResponse>

    //위시리스트 폴더 목록 삭제 *request 부분 추가 예정
    @POST("wishlist/folder-products/{folderId}/delete")
    fun deltoWishBoard(
        @Header("Authorization") accessToken: String,  // 헤더로 액세스 토큰 전달
        @Path("folderId") folderId: Int,
        @Body request: WishBoardListDelRequest
    ): Call<WishBoardDeleteResponse>
}