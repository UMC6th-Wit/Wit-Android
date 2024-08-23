package com.umc.umc_6th_wit_android.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.remote.product.ProductResult
import com.umc.umc_6th_wit_android.data.remote.product.ProductService
import com.umc.umc_6th_wit_android.databinding.FragmentProductDetailBinding
import com.umc.umc_6th_wit_android.product.ProductView
import com.umc.umc_6th_wit_android.product.ReviewMinFragment
import com.umc.umc_6th_wit_android.product.ReviewZeroFragment
import com.umc.umc_6th_wit_android.wish.CartItem

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private var reviewCount: Int = 0 // 현재 리뷰 개수, 페이지 로딩 시 마다

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
        binding.productDetailNameTv.text = arguments?.getString("name")
        binding.productTypeTv.text = arguments?.getString("product_type")
        binding.productCountryTv.text = arguments?.getString("manufacturing_country")
        reviewCount = arguments?.getString("review_count")?.toIntOrNull() ?: 0
        val id = arguments?.getString("id")?.toIntOrNull() ?: -1

        binding.productReviewSelectTv.setOnClickListener {
            val fragment = if (reviewCount == 0) {
                ReviewZeroFragment()  // ReviewZeroFragment로 이동
            } else {
                ReviewMinFragment()  // ReviewMinFragment로 이동
            }

            if(fragment == ReviewMinFragment())
                ReviewMinFragment.newInstance(id) //리뷰 오버뷰 프레그먼트에 상품 id 전달e

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_product_detail, fragment)  // fragment_container = 현재 프래그먼트를 표시하는 뷰의 ID
                .addToBackStack(null)  // 뒤로 가기 버튼을 사용하여 이전 프래그먼트로 돌아가기
                .commit()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(id: String, name: String, product_type: String,review_count: String, manufacturing_country : String): ProductDetailFragment {
            val fragment = ProductDetailFragment()
            val args = Bundle().apply {
                putString("id", id)
                putString("name", name)
                putString("product_type", product_type)
                putString("review_count", review_count)
                putString("manufacturing_country", manufacturing_country)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
