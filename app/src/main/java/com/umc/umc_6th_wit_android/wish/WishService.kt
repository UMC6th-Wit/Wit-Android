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
    fun getWishList(accessToken: String, cursor: Int?, limit: Int?){
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.getWishList("Bearer $accessToken", cursor, limit).enqueue(object : Callback<WishResponse>{
            override fun onResponse(call: Call<WishResponse>, response: Response<WishResponse>) {
                if (response.body()?.message.equals("Products retrieved successfully")) {
                    val wishResponse: WishResponse = response.body()!!

                    Log.d("WISHLIST200-RESPONSE", wishResponse.toString())

                    when (val message = wishResponse.message) {
                        "Products retrieved successfully" -> wishView.onGetWishListSuccess(message, wishResponse.data)
                        else -> {
                            wishView.onGetWishListFailure(message, wishResponse.message)
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
    fun getWishBoardList(accessToken: String,cursor: Int?, limit: Int?){
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.getWishBoardList("Bearer $accessToken", cursor, limit).enqueue(object : Callback<WishBoardResponse>{
            override fun onResponse(call: Call<WishBoardResponse>, response: Response<WishBoardResponse>) {
                if (response.body()?.message.equals("Folders retrieved successfully")) {
                    val wishBoardResponse: WishBoardResponse = response.body()!!

                    Log.d("WISHLIST200-RESPONSE", wishBoardResponse.toString())

                    when (val message = wishBoardResponse.message) {
                        "Folders retrieved successfully" -> wishView.onGetWishBoardListSuccess(message, wishBoardResponse.data)
                        else -> {
                            wishView.onGetWishBoardListFailure(message, wishBoardResponse.message)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<WishBoardResponse>, t: Throwable) {
                Log.d("WISHLIST200-ERROR", t.message.toString())
            }
        })
    }

    //위시리스트 폴더 상세 조회
    fun getWishBoard(accessToken: String,folderId: Int, cursor: Int?, limit: Int?) {
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.getWishBoard("Bearer $accessToken", folderId, cursor, limit).enqueue(object : Callback<WishResponse> {
            override fun onResponse(call: Call<WishResponse>, response: Response<WishResponse>) {
                Log.d("folder", response.toString())
                if (response.body()?.message.equals("Products retrieved successfully")) {
                    val wishItemResponse: WishResponse = response.body()!!

                    Log.d("FOLDER300-RESPONSE", wishItemResponse.toString())

                    when (val message = wishItemResponse.message) {
                        "Products retrieved successfully" -> wishListView.onGetWishBoardSuccess(message, wishItemResponse.data)
                        else -> {
                            wishListView.onGetWishBoardFailure(message, wishItemResponse.message)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<WishResponse>, t: Throwable) {
                //실패 처리
                Log.d("FOLDER300-ERROR", t.message.toString())
            }
        })
    }

    //위시리스트 폴더 상품 담기
    fun postWishtoBoard(accessToken: String,request: WishListAddRequest) {
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.postWishtoBoard("Bearer $accessToken", request).enqueue(object : Callback<WishBoardResponse> {
            override fun onResponse(call: Call<WishBoardResponse>, response: Response<WishBoardResponse>) {
                if (response.message().equals("폴더에 제품이 성공적으로 추가되었습니다")) {
                    val wishToBoardResponse: WishBoardResponse = response.body()!!

                    Log.d("FOLDER100-RESPONSE", wishToBoardResponse.toString())

                    when (val message = wishToBoardResponse.message) {
                        "폴더에 제품이 성공적으로 추가되었습니다" -> {
                            wishView.onPostWishtoBoardSuccess(message, wishToBoardResponse.data)
                            wishListView.onPostWishtoBoardSuccess(message, wishToBoardResponse.data)
                        }
                        else -> {
                            wishView.onPostWishtoBoardFailure(message, wishToBoardResponse.message)
                            wishListView.onPostWishtoBoardFailure(message, wishToBoardResponse.message)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<WishBoardResponse>, t: Throwable) {
                // 실패 처리
                Log.d("FOLDER100-ERROR", t.message.toString())
            }
        })
    }

    //위시리스트 폴더 생성
    fun postWishListCreate(accessToken: String,request: WishListCreateRequest) {
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.postWishListCreate("Bearer $accessToken", request).enqueue(object : Callback<WishBoardResponse> {
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
    fun postWishListReName(accessToken: String,request: WishListEditRequest) {
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.postWishListReName("Bearer $accessToken", request).enqueue(object : Callback<WishBoardNameResponse> {
            override fun onResponse(call: Call<WishBoardNameResponse>, response: Response<WishBoardNameResponse>) {
                if (response.message().equals("Folder name updated successfully")) {
                    val wishListReNameResponse: WishBoardNameResponse = response.body()!!

                    Log.d("FOLDER201-RESPONSE", wishListReNameResponse.toString())

                    when (val message = wishListReNameResponse.message) {
                        "Folder name updated successfully" -> {
                            wishView.onPostWishListReNameSuccess(message, wishListReNameResponse.message)
                        }
                        else -> {
                            wishView.onPostWishListReNameFailure(message, wishListReNameResponse.message)
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
    fun delWishBoardList(accessToken: String, folderId: List<Int>) {
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.delWishBoardList("Bearer $accessToken", folderId).enqueue(object : Callback<WishBoardDeleteResponse> {
            override fun onResponse(call: Call<WishBoardDeleteResponse>, response: Response<WishBoardDeleteResponse>) {
                if (response.message().equals("Folders deleted successfully")) {
                    val wishBoardListResponse: WishBoardDeleteResponse = response.body()!!

                    Log.d("FOLDER201-RESPONSE", wishBoardListResponse.toString())

                    when (val message = wishBoardListResponse.message) {
                        "Folders deleted successfully" -> {
                            wishView.onDeleteWishBoardListSuccess(message, wishBoardListResponse.message)
                        }
                        else -> {
                            wishView.onDeleteWishBoardListFailure(message, wishBoardListResponse.message)
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
    fun deltoWishBoard(accessToken: String, folderId: Int, productIds: List<Int>) {
        val wishService = getInstance().create(WishRetrofitInterfaces::class.java)

        wishService.deltoWishBoard("Bearer $accessToken", folderId, productIds).enqueue(object : Callback<WishBoardDeleteResponse> {
            override fun onResponse(call: Call<WishBoardDeleteResponse>, response: Response<WishBoardDeleteResponse>) {
                if (response.message().equals("Products deleted successfully")) {
                    val wishBoardListResponse: WishBoardDeleteResponse = response.body()!!

                    Log.d("FOLDER301-RESPONSE", wishBoardListResponse.toString())

                    when (val message = wishBoardListResponse.message) {
                        "Products deleted successfully" -> {
                            wishListView.onDeleteToWishBoardSuccess(message, wishBoardListResponse.message)
                        }
                        else -> {
                            wishListView.onDeleteToWishBoardFailure(message, wishBoardListResponse.message)
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