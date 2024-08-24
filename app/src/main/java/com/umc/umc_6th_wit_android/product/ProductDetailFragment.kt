package com.umc.umc_6th_wit_android.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentProductDetailBinding
import com.umc.umc_6th_wit_android.product.ReviewMinFragment
import com.umc.umc_6th_wit_android.product.ReviewZeroFragment
import java.util.Optional

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private var reviewCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달받은 데이터로 UI 업데이트
        binding.productDetailNameTv.text = arguments?.getString("name") ?: "Unknown"
        binding.productTypeTv.text = arguments?.getString("product_type") ?: "Unknown"
        binding.productCountryTv.text = arguments?.getString("manufacturing_country") ?: "Unknown"

        reviewCount = arguments?.getString("review_count")?.toIntOrNull() ?: 0
        val id = arguments?.getString("id")?.toIntOrNull() ?: -1

        binding.productReviewSelectTv.setOnClickListener {

            val fragment = if (reviewCount == 0) {

                ReviewZeroFragment(id)  // ReviewZeroFragment로 이동
            } else {
//                ReviewMinFragment.newInstance(id) // ReviewMinFragment로 이동
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_product_detail, ReviewMinFragment(id))
                    .commit()
            }

//            if(fragment == ReviewMinFragment(id))
//                ReviewMinFragment.newInstance(id) //리뷰 오버뷰 프레그먼트에 상품 id 전달
//
//            parentFragmentManager.beginTransaction()
//                .replace(R.id.fragment_product_detail, fragment)
//                .addToBackStack(null)
//                .commit()

            if(fragment == ReviewMinFragment(id))
                ReviewMinFragment.newInstance(id) //리뷰 오버뷰 프레그먼트에 상품 id 전달e

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object { //변수 옵션으로 사용 가능하게
        fun newInstance(
            id: String,
            name: String,
            product_type: String,
            review_count: Int,
            manufacturing_country: String
        ): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            val args = Bundle().apply {
                putString("id", id)
                putString("name", name)
                putString("product_type", product_type)
                putString("review_count", review_count.toString())
                putString("manufacturing_country", manufacturing_country)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
