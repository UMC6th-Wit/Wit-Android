package com.umc.umc_6th_wit_android.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentOnboarding1Binding

class OnboardingFragment1 : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: FragmentOnboarding1Binding

    private val selectedImages = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboarding1Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = requireActivity().findViewById(R.id.onboarding_viewpager)
        val nextButton = binding.onboarding1Next
        updateNextButtonState()

        nextButton.setOnClickListener {
            if (nextButton.isClickable) {
                //선택한 정보 백엔드에 전송 처리 부분

                val currentItem = viewPager.currentItem
                viewPager.currentItem = currentItem + 1
            }
        }

        nextButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            viewPager.currentItem = currentItem + 1
        }

        val imageViews = listOf(
            binding.onboarding1Img1,
            binding.onboarding1Img2,
            binding.onboarding1Img3,
            binding.onboarding1Img4
        )

        val imageNames = listOf("img1", "img2", "img3", "img4")
        val imageResources = listOf(
            R.drawable.onboarding1_img1,
            R.drawable.onboarding1_img2,
            R.drawable.onboarding1_img3,
            R.drawable.onboarding1_img4
        )
        val selectedImageResources = listOf(
            R.drawable.onboarding1_img1_selected,
            R.drawable.onboarding1_img2_selected,
            R.drawable.onboarding1_img3_selected,
            R.drawable.onboarding1_img4_selected

        )

        imageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                val imageName = imageNames[index]
                if (selectedImages.contains(imageName)) {
                    selectedImages.remove(imageName)
                    imageView.setImageResource(imageResources[index])
                } else {
                    selectedImages.add(imageName)
                    imageView.setImageResource(selectedImageResources[index])
                }
                updateNextButtonState()
            }
        }
    }

    private fun updateNextButtonState() {
        val nextButton = binding.onboarding1Next
        if (selectedImages.isNotEmpty()) {
            nextButton.setImageResource(R.drawable.next_button_on) // 활성화된 상태의 이미지 리소스
            nextButton.isClickable = true
        } else {
            nextButton.setImageResource(R.drawable.next_button_off) // 비활성화된 상태의 이미지 리소스
            nextButton.isClickable = false
        }
    }



}