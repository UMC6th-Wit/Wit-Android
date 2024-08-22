package com.umc.umc_6th_wit_android.wish

import retrofit2.*
import retrofit2.http.*

interface WishRetrofitInterfaces {

    //위시리스트 장바구니 목록 조회
    @GET("wishlist/{user_id}")
    fun getWishList(
        @Path("user_id") userId: Int,
        @Query("cursor") cursor: Int?,
        @Query("limit") limit: Int?
    ): Call<WishResponse>

    //제품 장바구니에 담기
//    @POST("product/add-cart/{product_id}")
//    fun addCart(
//        @Path("product_id") productId: Int,
//        @Body userId: Int
//    ): Call<CartResponse>

    //제품 장바구니에 빼기
//    @POST("product/delete-cart/{product_id}")
//    fun delCart(
//        @Path("product_id") productId: Int,
//        @Body userId: Int
//    ): Call<WishBoardDeleteResponse>

    //위시리스트 폴더 목록 조회
    @GET("wishlist/user-folders/{user_id}")
    fun getWishBoardList(
        @Path("user_id") userId: Int,
        @Query("cursor") cursor: Int?,
        @Query("limit") limit: Int?
    ): Call<WishBoardResponse>

    //위시리스트 폴더 상세 조회
    @GET("wishlist/folder-products/{user_id}/{folderId}")
    fun getWishBoard(
        @Path("user_id") userId: Int,
        @Path("folderId") folderId: Int,
        @Query("cursor") cursor: Int?,
        @Query("limit") limit: Int?
    ): Call<WishResponse>

    //위시리스트 폴더 생성
    @POST("wishlist/create-folder/{user_id}")
    fun postWishListCreate(
        @Path("user_id") userId: Int,
        @Body request: WishListCreateRequest
    ): Call<WishBoardResponse>

    //위시리스트 폴더 상품 담기
    @POST("wishlist/add-product/{user_id}")
    fun postWishtoBoard(
        @Path("user_id") userId: Int,
        @Body request: WishListAddRequest
    ): Call<WishBoardResponse>

    //위시리스트 폴더 이름 변경
    @POST("wishlist/update-folder/{user_id}")
    fun postWishListReName(
        @Path("user_id") userId: Int,
        @Body request: WishListEditRequest
    ): Call<WishBoardNameResponse>

    //위시리스트 폴더 삭제 *request 부분 추가 예정
    @POST("wishlist/delete-folders/{user_id}")
    fun delWishBoardList(
        @Path("user_id") userId: Int,
        @Body folderIds: List<Int>
    ): Call<WishBoardDeleteResponse>

    //위시리스트 폴더 목록 삭제 *request 부분 추가 예정
    @POST("wishlist/folder-products/{folderId}")
    fun deltoWishBoard(
        @Path("folderId") folderId: Int,
        @Body productIds: List<Int>
    ): Call<WishBoardDeleteResponse>
}