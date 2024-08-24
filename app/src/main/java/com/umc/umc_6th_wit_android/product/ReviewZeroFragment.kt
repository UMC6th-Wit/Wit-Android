package com.umc.umc_6th_wit_android.product

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.remote.product.ProductResult
import com.umc.umc_6th_wit_android.data.remote.product.ProductService
import com.umc.umc_6th_wit_android.databinding.FragmentReviewZeroBinding
import com.umc.umc_6th_wit_android.home.ProductDetailFragment

class ReviewZeroFragment(private val productId: Int) : Fragment() {

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
                .replace(R.id.fragment_product_detail, fragment)
                .commit()
        }

        binding.goToReviewWriteIv.setOnClickListener {
            val intent = Intent(requireContext(), ReviewWriteActivity::class.java)
            intent.putExtra("id", productId)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

