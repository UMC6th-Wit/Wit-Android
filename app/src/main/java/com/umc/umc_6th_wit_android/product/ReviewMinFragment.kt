package com.umc.umc_6th_wit_android.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.umc_6th_wit_android.data.remote.product.ProductResult
import com.umc.umc_6th_wit_android.data.remote.product.ProductService
import com.umc.umc_6th_wit_android.data.remote.product.Review
import com.umc.umc_6th_wit_android.data.remote.product.ReviewOverviewResult
import com.umc.umc_6th_wit_android.databinding.FragmentReviewMinBinding
import com.umc.umc_6th_wit_android.home.ProductDetailFragment
import com.umc.umc_6th_wit_android.wish.CartItem
import okhttp3.internal.format

class ReviewMinFragment(private val productId: Int) : Fragment(), ProductView, ReviewOverviewView {

    private var _binding: FragmentReviewMinBinding? = null
    private val binding get() = _binding!!
    private var id = productId
    private var name: String = ""
    private var product_type: String = ""
    private var review_count: Int? = null
    private var manufacturing_country: String = ""

    private lateinit var reviewitems: List<Review>
    private lateinit var imageitems: List<String>

    companion object {
        fun newInstance(id: Int): ReviewMinFragment {
            val fragment = ReviewMinFragment(id)
            val args = Bundle().apply {
                putInt("id", id)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewMinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Reviewitems가 초기화되지 않았을 경우 빈 리스트로 초기화
        if (!::reviewitems.isInitialized) {
            reviewitems = emptyList()
        }

        if (!::imageitems.isInitialized) {
            imageitems = emptyList()
        }

        binding.productDetailSelectTv.setOnClickListener {

            val fragment = ProductDetailFragment.newInstance(id.toString(), name, product_type, review_count!!, manufacturing_country) // id를 넘기기, 넘길때 0으로 넣어서 0으로 초기화됨

//            parentFragmentManager.beginTransaction()
//                .replace(
//                    R.id.fragment_product_detail,
//                    ProductDetailFragment()
//                )
//                .commit()
        }

        binding.goToReviewBtnIv.setOnClickListener {
            val intent = Intent(requireContext(), ReviewOnlyActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        binding.moreReviewBtnIv.setOnClickListener {
            val intent = Intent(requireContext(), ReviewOnlyActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }


        binding.reviewMinImagesRv.layoutManager = LinearLayoutManager(requireContext())
        // 어댑터 설정1
        val imageAdapter = ReviewMinImagesRVAdapter(imageitems)
        binding.reviewMinRv.adapter = imageAdapter

        binding.reviewMinRv.layoutManager = LinearLayoutManager(requireContext())
        // 어댑터 설정2
        val reviewAdapter = ReviewMinRVAdapter(reviewitems)
        binding.reviewMinRv.adapter = reviewAdapter

    }

    override fun onGetProductSuccess(code: String, result: ProductResult) {
        Log.d("Product-SUCCESS", code + result.name)
        id = result.id
        name = result.name
        product_type = result.product_type
        review_count = result.review_count
        manufacturing_country = result.manufacturing_country

        Log.d("ReviewMinFragProduct_Id", result.id.toString())

        //정보 가져 오는데 성공 -> 뷰에 반영
        binding.reviewRateTv.text = "${result.average_rating}"
        binding.reciewNumTv.text = "${result.review_count}"
        binding.productReviewSelectTv.text = "리뷰(${result.review_count})"
        binding.ratingBar.rating = (Math.round(result.average_rating * 10) / 10.0).toFloat()
        reviewitems = result.top_reviews
    }

    override fun onGetProductFailure(code: String, message: String) {
        Log.d("ReviewMin-FAILURE", code)
    }

    override fun onResume() {
        super.onResume()
        getProductDetail()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getProductDetail() {
        if (id != null && id != -1) {
            Log.d("ReviewMinFragment", "Ok id received")

            // 올바른 Context를 사용하여 ProductService 초기화
            val context = context ?: return // Context가 null이 아닌지 확인
            val productService = ProductService(context)

            productService.setProductDetailView(this)
            productService.getProductDetail(id!!)
        } else {
            Log.d("ReviewMinFragment", "No valid id received")
        }
    }

    override fun onGetReviewOverviewSuccess(code: String, result: ReviewOverviewResult) {
        Log.d("ReviewOverview-SUCCESS", code + result)
        imageitems = result.latestImages
    }

    override fun onGetReviewOverviewFailure(code: String, message: String) {
        Log.d("ReviewOverview-Failure", code)
    }

    override fun onPostAddCartSuccess(code: String, response: CartItem) {
        TODO("Not yet implemented")
    }

    override fun onPostAddCartFailure(code: String, error: String) {
        TODO("Not yet implemented")
    }

    override fun onPostDelCartSuccess(code: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun onPostDelCartFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }
}

