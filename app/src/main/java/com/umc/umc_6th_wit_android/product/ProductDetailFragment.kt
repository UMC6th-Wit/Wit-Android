package com.umc.umc_6th_wit_android.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.remote.product.ProductResult
import com.umc.umc_6th_wit_android.databinding.FragmentProductDetailBinding
import com.umc.umc_6th_wit_android.product.ProductView
import com.umc.umc_6th_wit_android.product.ReviewMinFragment
import com.umc.umc_6th_wit_android.product.ReviewZeroFragment
import com.umc.umc_6th_wit_android.wish.CartItem

class ProductDetailFragment : Fragment(), ProductView {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    private var reviewCount: Int = 2 // 현재 리뷰 개수, 페이지 로딩 시 마다

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productReviewSelectTv.setOnClickListener {
            val fragment = if (reviewCount == 0) {
                ReviewZeroFragment()  // ReviewZeroFragment로 이동
            } else {
                ReviewMinFragment()  // ReviewMinFragment로 이동
            }

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

    override fun onGetProductSuccess(code: String, result: ProductResult) {
        Log.d("ProductFragment-SUCCESS", code + result.name)

        //정보 가져 오는데 성공 -> 뷰에 반영
        binding.productDetailNameTv.text = "${result.name}"
        binding.productTypeTv.text = "${result.product_type}"
        binding.productCountryTv.text = "${result.manufacturing_country}"
    }

    override fun onGetProductFailure(code: String, message: String) {
        Log.d("ProductFragment-FAILURE", code)
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
