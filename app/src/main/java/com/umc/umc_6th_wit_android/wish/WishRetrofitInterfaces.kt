package com.umc.umc_6th_wit_android.wish

import retrofit2.*
import retrofit2.http.*

interface WishRetrofitInterfaces {

    //위시리스트 장바구니 목록 조회
    @GET("wishlist/{user_id}")
    fun getWishList(@Path("user_id") userId: Int): Call<WishResponse>

    //위시리스트 폴더 목록 조회
    @GET("wishboard/user-folders/{user_id}")
    fun getWishBoardList(@Path("user_id") userId: Int): Call<WishBoardResponse>

    //위시리스트 폴더 상세 조회
    @GET("wishlist/folder-products/{folderId}")
    fun getWishBoard(@Path("folderId") folderId: Int): Call<WishBoardResponse>

    //위시리스트 폴더 상품 담기
    @POST("wishlist/create-folder/{user_id}")
    fun postWishtoBoard(
        @Body wishboard: Wishboard,
        @Body wishitems: List<WishItem>
    ): Call<WishBoardResponse>

    //위시리스트 폴더 생성
    @POST("wishlist/create-folder/{user_id}")
    fun postWishListCreate(
        @Path("user_id") userId: Int,
        @Body request: WishListCreateRequest
    ): Call<WishBoardResponse>

    //위시리스트 폴더 이름 변경
    @POST("wishlist/update-folder/{user_id}")
    fun postWishListReName(
        @Body folderId: Int,
        @Body newFolderName: String
    ): Call<WishBoardNameResponse>

    //위시리스트 폴더 삭제 *request 부분 추가 예정
    @DELETE("wishlist/delete-folders/{user_id}")
    fun delWishBoardList(
        @Path("user_id") userId: Int,
        @Body folderId: List<Int>
    ): Call<WishBoardDeleteResponse>

    //위시리스트 폴더 목록 삭제 *request 부분 추가 예정
    @DELETE("wishlist/{folderId}")
    fun deltoWishBoard(
        @Path("folderId") folderId: Int,
        @Body productIds: List<Int>
    ): Call<WishBoardDeleteResponse>
}