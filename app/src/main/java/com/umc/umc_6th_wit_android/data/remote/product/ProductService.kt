package com.umc.umc_6th_wit_android.data.remote.product

import android.content.Context
import android.util.Log
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.network.TokenRetrofitManager
import com.umc.umc_6th_wit_android.product.ProductDetailActivity
import com.umc.umc_6th_wit_android.product.ProductView
import com.umc.umc_6th_wit_android.product.ReviewCreationView
import com.umc.umc_6th_wit_android.product.ReviewOverviewView
import com.umc.umc_6th_wit_android.product.ReviewPageView
import com.umc.umc_6th_wit_android.product.ReviewView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductService(private val context: ProductDetailActivity) { //매개변수에 , private val productServiceApi: ProductRetrofitInterface 필요하면 사용

    private lateinit var productView: ProductView
    private lateinit var reviewView: ReviewView
    private lateinit var reviewoverviewView: ReviewOverviewView
    private lateinit var reviewcreationView: ReviewCreationView
    private lateinit var reviewpageView: ReviewPageView

    // SharedPreferences 및 TokenManager 설정
    val sharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    val tokenManager = TokenManager(sharedPreferences)

    // 뷰 설정 메소드
    fun setProductDetailView(view: ProductView) {
        this.productView = view
    }

    fun setReviewView(view: ReviewView) {
        this.reviewView = view
    }

    fun setReviewOverviewView(view: ReviewOverviewView) {
        this.reviewoverviewView = view
    }

    fun setReviewCreationView(view: ReviewCreationView) {
        this.reviewcreationView = view
    }
    fun setReviewPageView(view: ReviewPageView) {
        this.reviewpageView = view
    }

    // 제품 상세 정보 불러오기
    fun getProductDetail(productId: Int) {
        // TokenManager에서 저장된 액세스 토큰을 가져옴
        val accessToken = tokenManager.getAccessToken()
        if (accessToken != null) {

            val retrofit = TokenRetrofitManager(context)
            val productServiceApi = retrofit.create(ProductRetrofitInterface::class.java)
            productServiceApi.getProductDetail("Bearer $accessToken", productId).enqueue(object : Callback<ProductResponse> {
                override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                    Log.d("product_response", response.body().toString())
                    if (response.isSuccessful && response.body()?.message == "Product retrieved successfully") {
                        val productResponse: ProductResponse = response.body()!!

                        Log.d("PRODUCT_DETAIL", productResponse.toString())

                        productView.onGetProductSuccess(response.body()?.code.toString(), productResponse.result)
                    } else {
                        productView.onGetProductFailure(response.body()?.code.toString(), response.body()?.message ?: "Unknown error")
                    }
                }

                override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                    Log.d("PRODUCT_DETAIL_ERROR", t.message.toString())
                    productView.onGetProductFailure("500", t.message ?: "Unknown error")
                }
            })
        }
    }

    // 제품 리뷰 목록 불러오기 (베스트순, 최신순)
    fun getProductReviews(productId: Int) {
        val productServiceApi = getInstance().create(ProductRetrofitInterface::class.java)
        productServiceApi.getProductReviews(productId).enqueue(object : Callback<ProductReviewsResponse> {
            override fun onResponse(call: Call<ProductReviewsResponse>, response: Response<ProductReviewsResponse>) {
                if (response.isSuccessful) {
                    val productReviewsResponse: ProductReviewsResponse = response.body()!!

                    Log.d("PRODUCT_REVIEWS", productReviewsResponse.toString())

                    reviewView.onGetReviewsSuccess(response.code().toString(), productReviewsResponse.result)
                } else {
                    reviewView.onGetReviewsFailure(response.code().toString(), response.message())
                }
            }

            override fun onFailure(call: Call<ProductReviewsResponse>, t: Throwable) {
                Log.d("PRODUCT_REVIEWS_ERROR", t.message.toString())
                reviewView.onGetReviewsFailure("500", t.message ?: "Unknown error")
            }
        })
    }

    // 리뷰 작성하기 (이미지 포함)
    fun createReview(productId: Int, rating: Int, content: String, images: String) {
        val productServiceApi = getInstance().create(ProductRetrofitInterface::class.java)
        productServiceApi.createReview(productId, rating, content, images).enqueue(object : Callback<ReviewCreationResponse> {
            override fun onResponse(call: Call<ReviewCreationResponse>, response: Response<ReviewCreationResponse>) {
                if (response.isSuccessful) {
                    val reviewCreationResponse: ReviewCreationResponse = response.body()!!

                    Log.d("REVIEW_CREATION", reviewCreationResponse.toString())

                    reviewcreationView.onPostReviewCreationSuccess(response.body()?.code.toString(), reviewCreationResponse.result)
                } else {
                    reviewcreationView.onPostReviewCreationFailure(response.body()?.code.toString(), response.body()?.message ?: "Unknown error")
                }
            }

            override fun onFailure(call: Call<ReviewCreationResponse>, t: Throwable) {
                Log.d("REVIEW_CREATION_ERROR", t.message.toString())
                reviewcreationView.onPostReviewCreationFailure("500", t.message ?: "Unknown error")
            }
        })
    }

    // 리뷰 작성 페이지 불러오기
    fun getReviewPageData(productId: Int) {
        val productServiceApi = getInstance().create(ProductRetrofitInterface::class.java)
        productServiceApi.getReviewPageData(productId).enqueue(object : Callback<ReviewPageDataResponse> {
            override fun onResponse(call: Call<ReviewPageDataResponse>, response: Response<ReviewPageDataResponse>) {
                if (response.isSuccessful) {
                    val reviewPageDataResponse: ReviewPageDataResponse = response.body()!!

                    Log.d("REVIEW_PAGE_DATA", reviewPageDataResponse.toString())

                    reviewpageView.onGetReviewPageDataSuccess(response.body()?.code.toString(), reviewPageDataResponse.result)
                } else {
                    reviewpageView.onGetReviewPageDataFailure(response.body()?.code.toString(), response.body()?.message ?: "Unknown error")
                }
            }

            override fun onFailure(call: Call<ReviewPageDataResponse>, t: Throwable) {
                Log.d("REVIEW_PAGE_DATA_ERROR", t.message.toString())
                reviewpageView.onGetReviewPageDataFailure("500", t.message ?: "Unknown error")
            }
        })
    }

    // 상품 별점 불러오기 (별점별 리뷰 개수)
    fun getRatingStats(productId: Int) {
        val productServiceApi = getInstance().create(ProductRetrofitInterface::class.java)
        productServiceApi.getRatingStats(productId).enqueue(object : Callback<RatingStatsResponse> {
            override fun onResponse(call: Call<RatingStatsResponse>, response: Response<RatingStatsResponse>) {
                if (response.isSuccessful) {
                    val ratingStatsResponse: RatingStatsResponse = response.body()!!

                    Log.d("RATING_STATS", ratingStatsResponse.toString())

                    reviewView.onGetRatingStatsSuccess(response.body()?.code.toString(), ratingStatsResponse.result)
                } else {
                    reviewView.onGetRatingStatsFailure(response.body()?.code.toString(), response.body()?.message ?: "Unknown error")
                }
            }

            override fun onFailure(call: Call<RatingStatsResponse>, t: Throwable) {
                Log.d("RATING_STATS_ERROR", t.message.toString())
                reviewView.onGetRatingStatsFailure("500", t.message ?: "Unknown error")
            }
        })
    }

    // 리뷰에 도움이 돼요 누르기
    fun markReviewAsHelpful(productId: Int, reviewId: Int) {
        val productServiceApi = getInstance().create(ProductRetrofitInterface::class.java)
        productServiceApi.markReviewAsHelpful(productId, reviewId).enqueue(object : Callback<HelpfulResponse> {
            override fun onResponse(call: Call<HelpfulResponse>, response: Response<HelpfulResponse>) {
                if (response.isSuccessful) {
                    val helpfulResponse: HelpfulResponse = response.body()!!

                    Log.d("HELPFUL", helpfulResponse.toString())

                    reviewView.onPostHelpfulSuccess(response.code().toString(), helpfulResponse)
                } else {
                    reviewView.onPostHelpfulFailure(response.code().toString(), response.body()?.message ?: "Unknown error")
                }
            }

            override fun onFailure(call: Call<HelpfulResponse>, t: Throwable) {
                Log.d("HELPFUL_ERROR", t.message.toString())
                reviewView.onPostHelpfulFailure("500", t.message ?: "Unknown error")
            }
        })
    }

    // Retrofit 인스턴스 생성
    private fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://43.202.194.145/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

