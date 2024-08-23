package com.umc.umc_6th_wit_android.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.remote.product.ProductResult
import com.umc.umc_6th_wit_android.data.remote.product.ProductService
import com.umc.umc_6th_wit_android.data.remote.product.Review
import com.umc.umc_6th_wit_android.data.remote.product.ReviewOverviewResult
import com.umc.umc_6th_wit_android.databinding.FragmentReviewMinBinding
import com.umc.umc_6th_wit_android.home.ProductDetailFragment
import com.umc.umc_6th_wit_android.wish.CartItem

class ReviewMinFragment(id: Int) : Fragment(), ProductView, ReviewOverviewView {

    private var _binding: FragmentReviewMinBinding? = null
    private val binding get() = _binding!!
    private var id: Int? = null
    private lateinit var Reviewitems: List<Review>
    private lateinit var Imageitems: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewMinBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productDetailSelectTv.setOnClickListener {
            val fragment = ProductDetailFragment.newInstance(id.toString(), "", "", "") // id를 넘기기

            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fragment_product_detail,
                    fragment
                )  // fragment_container = 현재 프래그먼트를 표시하는 뷰의 ID
                .addToBackStack(null)  // 뒤로 가기 버튼을 사용하여 이전 프래그먼트로 돌아가기
                .commit()
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
        val imageAdapter = ReviewMinImagesRVAdapter(Imageitems)
        binding.reviewMinRv.adapter = imageAdapter

        binding.reviewMinRv.layoutManager = LinearLayoutManager(requireContext())
        // 어댑터 설정2
        val ReviewAdapter = ReviewMinRVAdapter(Reviewitems)
        binding.reviewMinRv.adapter = ReviewAdapter

    }

    override fun onGetProductSuccess(code: String, result: ProductResult) {
        Log.d("Product-SUCCESS", code + result.name)
        id = result.id
        Log.d("ReviewMinFragProduct_Id", result.id.toString())

        //정보 가져 오는데 성공 -> 뷰에 반영
        binding.reviewRateTv.text = "${result.average_rating}"
        binding.reciewNumTv.text = "${result.review_count}"
        binding.productReviewSelectTv.text = "리뷰(${result.review_count})"
        binding.ratingBar.rating = result.average_rating.toFloat()

        Reviewitems = result.top_reviews
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

    private fun getProductDetail() {
        val id: Int? = null

        if (id != -1) {
            Log.d("ReviewMinFragment", "Ok id received")
            val productService = ProductService(ProductDetailActivity())
            productService.setProductDetailView(this)
            productService.getProductDetail(id!!)

        } else {
            Log.d("ReviewMinFragment", "No id received")
        }
    }

    override fun onGetReviewOverviewSuccess(code: String, result: ReviewOverviewResult) {
        Log.d("ReviewOverview-SUCCESS", code + result)
        Imageitems = result.latestImages
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

