package com.umc.umc_6th_wit_android.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.umc_6th_wit_android.data.remote.product.HelpfulResponse
import com.umc.umc_6th_wit_android.data.remote.product.ProductService
import com.umc.umc_6th_wit_android.data.remote.product.RatingStatsResult
import com.umc.umc_6th_wit_android.data.remote.product.Review
import com.umc.umc_6th_wit_android.data.remote.product.ReviewsResult
import com.umc.umc_6th_wit_android.databinding.FragmentReviewBestBinding

class ReviewBestFragment : Fragment(), ReviewView {

    private var _binding: FragmentReviewBestBinding? = null
    private val binding get() = _binding!!
    private lateinit var Reviewitems: List<Review>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewBestBinding.inflate(inflater, container, false)
        return binding.root
    } override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.reviewBestRv.layoutManager = LinearLayoutManager(requireContext())
        // 어댑터 설정
        val adapter = ReviewOnlyAdapter(Reviewitems)
        binding.reviewBestRv.adapter = adapter
    }

    override fun onGetReviewsSuccess(code: String, result: ReviewsResult) {
        Reviewitems = result.reviews
    }

    override fun onGetReviewsFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun onGetRatingStatsSuccess(code: String, result: RatingStatsResult) {
        TODO("Not yet implemented")
    }

    override fun onGetRatingStatsFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun onPostHelpfulSuccess(code: String, result: HelpfulResponse) {
        TODO("Not yet implemented")
    }

    override fun onPostHelpfulFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }
    override fun onResume() {
        super.onResume()
        getProductReviews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getProductReviews(){
        val id: Int? = null
        if (id != -1) {
            Log.d("ReviewBestFragment", "Ok id received")
            val reviewsService = ProductService(ReviewOnlyActivity())
            reviewsService.setReviewView(this)  // 리뷰 데이터를 받을 인터페이스 설정
            reviewsService.getProductReviews(id!!)
        }else {
            Log.d("ReviewBestFragment", "No id received")
        }

    }
}

