package com.umc.umc_6th_wit_android.product

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.PriceActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentProductDetailBinding

class ProductDetailFragment : Fragment() {

    lateinit var binding: FragmentProductDetailBinding
    private var isHelpIv = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //button 같은 UI 초기화 부분 작성
        binding.comparisonBtnIv.setOnClickListener {
            val intent = Intent(activity, PriceActivity::class.java)
            startActivity(intent)
        }

        binding.heartBtnIv.setOnClickListener {
            // 하트 버튼 이미지 변경 로직
            if (isHelpIv) {
                binding.heartBtnIv.setImageResource(R.drawable.heart_btn_empty_image)
            } else {
                binding.heartBtnIv.setImageResource(R.drawable.heart_btn_full_image)
            }
            isHelpIv = !isHelpIv //하트 버튼 상태 변경

            //DB 하트 숫자 변경

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}