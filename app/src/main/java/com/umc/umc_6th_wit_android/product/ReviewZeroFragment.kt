package com.umc.umc_6th_wit_android.product

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentReviewZeroBinding
import com.umc.umc_6th_wit_android.home.ProductDetailFragment

class ReviewZeroFragment : Fragment() {

    private var _binding: FragmentReviewZeroBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReviewZeroBinding.inflate(inflater, container, false)
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

        binding.goToReviewWriteIv.setOnClickListener {
            val intent = Intent(requireContext(), ReviewWriteActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
