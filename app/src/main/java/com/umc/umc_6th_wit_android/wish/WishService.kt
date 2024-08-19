package com.umc.umc_6th_wit_android.wish

import android.util.Log
import com.umc.umc_6th_wit_android.NetworkModule.Companion.getInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishService {
    private lateinit var wishView: WishView
    private lateinit var wishListView: WishListView
    private lateinit var folderView: FolderView

    fun setWishView(wishView: WishView){
        this.wishView = wishView
    }

    fun setWishListView(wishListView: WishListView){
        this.wishListView = wishListView
    }

    fun setFolderView(folderView: FolderView){
        this.folderView = folderView
    }

    //위시리스트 장바구니 목록 조회
    private fun getWishList(userId: Int){
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.getWishList(userId).enqueue(object : Callback<WishResponse>{
            override fun onResponse(call: Call<WishResponse>, response: Response<WishResponse>) {
                if (response.isSuccessful) {
                    val wishResponse: WishResponse = response.body()!!

                    Log.d("WISHLIST200-RESPONSE", wishResponse.toString())

                    when (val code = wishResponse.code) {
                        "WISHLIST200" -> wishView.onGetWishListSuccess(code, wishResponse.data)
                        else -> {
                            wishView.onGetWishListFailure(code, wishResponse.message)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<WishResponse>, t: Throwable) {
                //실패처리
                Log.d("WISHLIST200-ERROR", t.message.toString())
            }
        })
    }

    //위시리스트 폴더 목록 조회
    private fun getWishBoardList(userId: Int){
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.getWishBoardList(userId).enqueue(object : Callback<WishBoardResponse>{
            override fun onResponse(call: Call<WishBoardResponse>, response: Response<WishBoardResponse>) {
                if (response.isSuccessful) {
                    val wishBoardResponse: WishBoardResponse = response.body()!!

                    Log.d("WISHLIST200-RESPONSE", wishBoardResponse.toString())

//                    when (val code = wishBoardResponse.code) {
//                        "WISHLIST200" -> wishView.onGetWishBoardListSuccess(code, wishBoardResponse.data)
//                        else -> {
//                            wishView.onGetWishBoardListFailure(code, wishBoardResponse.message)
//                        }
//                    }
                }
            }

            override fun onFailure(call: Call<WishBoardResponse>, t: Throwable) {
                Log.d("WISHLIST200-ERROR", t.message.toString())
            }
        })
    }

    //위시리스트 폴더 상세 조회
    private fun getWishBoard(folderId: Int) {
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.getWishBoard(folderId).enqueue(object : Callback<WishBoardResponse> {
            override fun onResponse(call: Call<WishBoardResponse>, response: Response<WishBoardResponse>) {
                if (response.isSuccessful) {
                    val wishBoardResponse: WishBoardResponse = response.body()!!

                    Log.d("FOLDER300-RESPONSE", wishBoardResponse.toString())

//                    when (val code = wishBoardResponse.code) {
//                        "FOLDER300" -> wishListView.onGetWishBoardSuccess(code, wishBoardResponse.data)
//                        else -> {
//                            wishListView.onGetWishBoardFailure(code, wishBoardResponse.message)
//                        }
//                    }
                }
            }

            override fun onFailure(call: Call<WishBoardResponse>, t: Throwable) {
                //실패 처리
                Log.d("FOLDER300-ERROR", t.message.toString())
            }
        })
    }

    //위시리스트 폴더 상품 담기
    private fun postWishtoBoard(wishboard: Wishboard, wishitems: List<WishItem>) {
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.postWishtoBoard(wishboard, wishitems).enqueue(object : Callback<WishBoardResponse> {
            override fun onResponse(call: Call<WishBoardResponse>, response: Response<WishBoardResponse>) {
                if (response.isSuccessful) {
                    val wishToBoardResponse: WishBoardResponse = response.body()!!

                    Log.d("FOLDER100-RESPONSE", wishToBoardResponse.toString())

//                    when (val code = wishToBoardResponse.code) {
//                        "FOLDER100" -> {
//                            wishView.onPostWishtoBoardSuccess(code, wishToBoardResponse.data)
//                            wishListView.onPostWishtoBoardSuccess(code, wishToBoardResponse.data)
//                        }
//                        else -> {
//                            wishView.onPostWishtoBoardFailure(code, wishToBoardResponse.message)
//                            wishListView.onPostWishtoBoardFailure(code, wishToBoardResponse.message)
//                        }
//                    }
                }
            }

            override fun onFailure(call: Call<WishBoardResponse>, t: Throwable) {
                // 실패 처리
                Log.d("FOLDER100-ERROR", t.message.toString())
            }
        })
    }

    //위시리스트 폴더 생성
    fun postWishListCreate(userId: Int, request: WishListCreateRequest) {
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.postWishListCreate(userId, request).enqueue(object : Callback<WishBoardResponse> {
            override fun onResponse(call: Call<WishBoardResponse>, response: Response<WishBoardResponse>) {
                if (response.message().equals("Folder created successfully")) {
                    val wishListCreateResponse: WishBoardResponse = response.body()!!

                    Log.d("FOLDER200-RESPONSE", wishListCreateResponse.toString())

                    when (val message = wishListCreateResponse.message) {
                        "Folder created successfully" -> {
                            folderView.onPostWishListCreateSuccess(message, wishListCreateResponse.data)
                        }
                        else -> {
                            folderView.onPostWishListCreateFailure(message, wishListCreateResponse.message)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<WishBoardResponse>, t: Throwable) {
                // 실패 처리
                Log.d("FOLDER200-ERROR", t.message.toString())
            }
        })
    }

    //위시리스트 폴더 이름 변경
    private fun postWishListReName(folderId: Int, newFolderName: String) {
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.postWishListReName(folderId, newFolderName).enqueue(object : Callback<WishBoardNameResponse> {
            override fun onResponse(call: Call<WishBoardNameResponse>, response: Response<WishBoardNameResponse>) {
                if (response.isSuccessful) {
                    val wishListReNameResponse: WishBoardNameResponse = response.body()!!

                    Log.d("FOLDER201-RESPONSE", wishListReNameResponse.toString())

                    when (val code = wishListReNameResponse.code) {
                        "FOLDER201" -> {
                            wishView.onPostWishListReNameSuccess(code, wishListReNameResponse.message)
                        }
                        else -> {
                            wishView.onPostWishListReNameFailure(code, wishListReNameResponse.message)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<WishBoardNameResponse>, t: Throwable) {
                // 실패 처리
                Log.d("FOLDER201-ERROR", t.message.toString())
            }
        })
    }

    //위시리스트 폴더 삭제
    private fun delWishBoardList(userId: Int, folderId: List<Int>) {
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.delWishBoardList(userId, folderId).enqueue(object : Callback<WishBoardDeleteResponse> {
            override fun onResponse(call: Call<WishBoardDeleteResponse>, response: Response<WishBoardDeleteResponse>) {
                if (response.isSuccessful) {
                    val wishBoardListResponse: WishBoardDeleteResponse = response.body()!!

                    Log.d("FOLDER201-RESPONSE", wishBoardListResponse.toString())

                    when (val code = wishBoardListResponse.code) {
                        "FOLDER201" -> {
                            wishView.onDeleteWishBoardListSuccess(code, wishBoardListResponse.message)
                        }
                        else -> {
                            wishView.onDeleteWishBoardListFailure(code, wishBoardListResponse.message)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<WishBoardDeleteResponse>, t: Throwable) {
                // 실패 처리
                Log.d("FOLDER201-ERROR", t.message.toString())
            }
        })
    }

    //위시리스트 폴더 목록 삭제
    private fun deltoWishBoard(folderId: Int, productIds: List<Int>) {
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.deltoWishBoard(folderId, productIds).enqueue(object : Callback<WishBoardDeleteResponse> {
            override fun onResponse(call: Call<WishBoardDeleteResponse>, response: Response<WishBoardDeleteResponse>) {
                if (response.isSuccessful) {
                    val wishBoardListResponse: WishBoardDeleteResponse = response.body()!!

                    Log.d("FOLDER301-RESPONSE", wishBoardListResponse.toString())

                    when (val code = wishBoardListResponse.code) {
                        "FOLDER301" -> {
                            wishListView.onDeleteToWishBoardSuccess(code, wishBoardListResponse.message)
                        }
                        else -> {
                            wishListView.onDeleteToWishBoardFailure(code, wishBoardListResponse.message)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<WishBoardDeleteResponse>, t: Throwable) {
                // 실패 처리
                Log.d("FOLDER301-ERROR", t.message.toString())
            }
        })
    }


}