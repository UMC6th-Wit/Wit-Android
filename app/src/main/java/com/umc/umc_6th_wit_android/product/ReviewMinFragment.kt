package com.umc.umc_6th_wit_android.product

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.remote.product.ProductResult
import com.umc.umc_6th_wit_android.databinding.FragmentReviewMinBinding
import com.umc.umc_6th_wit_android.home.ProductDetailFragment

class ReviewMinFragment : Fragment(), ProductView {

    private var _binding: FragmentReviewMinBinding? = null
    private val binding get() = _binding!!

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
            val fragment = ProductDetailFragment()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_product_detail, fragment)  // fragment_container = 현재 프래그먼트를 표시하는 뷰의 ID
                .addToBackStack(null)  // 뒤로 가기 버튼을 사용하여 이전 프래그먼트로 돌아가기
                .commit()
        }

        binding.goToReviewBtnIv.setOnClickListener {
            val intent = Intent(requireContext(), ReviewOnlyActivity::class.java)
            startActivity(intent)
        }

        binding.moreReviewBtnIv.setOnClickListener{
            val intent = Intent(requireContext(), ReviewOnlyActivity::class.java)
            startActivity(intent)
        }

        val recyclerView = binding.reviewMinRv
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val itemList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        val adapter = ReviewMinAdapter(itemList)
        recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetProductSuccess(code: String, result: ProductResult) {
        Log.d("Product-SUCCESS", code + result.name)

        //정보 가져 오는데 성공 -> 뷰에 반영
        binding.reviewRateTv.text = "${result.average_rating.toFloat()}"
        binding.reciewNumTv.text = "${result.review_count}"
        binding.productReviewSelectTv.text = "리뷰(${result.review_count})"
        binding.ratingBar.rating = result.average_rating.toFloat()
    }

    override fun onGetProductFailure(code: String, message: String) {
        Log.d("Product-FAILURE", code)
    }
}
