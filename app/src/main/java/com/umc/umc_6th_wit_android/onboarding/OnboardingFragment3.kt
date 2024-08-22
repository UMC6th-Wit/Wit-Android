package com.umc.umc_6th_wit_android.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentOnboarding3Binding


class OnboardingFragment3 : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: FragmentOnboarding3Binding
    private val sharedViewModel: SharedOnboardingViewModel by activityViewModels()
    private val selectedImages = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOnboarding3Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = requireActivity().findViewById(R.id.onboarding_viewpager)
        val nextButton = binding.onboarding3Next
        updateNextButtonState()

        nextButton.setOnClickListener {
            if (nextButton.isClickable) {
                //선택한 정보 백엔드에 전송 처리 부분
                sharedViewModel.personalityImages.value = selectedImages.toMutableList()

                val currentItem = viewPager.currentItem
                viewPager.currentItem = currentItem + 1
            }
        }

        nextButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            viewPager.currentItem = currentItem + 1
        }

        val imageViews = listOf(
            binding.onboarding3Img1,
            binding.onboarding3Img2,
            binding.onboarding3Img3,
            binding.onboarding3Img4,
            binding.onboarding3Img5,
            binding.onboarding3Img6,
            binding.onboarding3Img7,
            binding.onboarding3Img8,
            binding.onboarding3Img9,
        )

        val imageNames = listOf("type1", "type2", "type3", "type4",
            "type5", "type6", "type7", "type8", "type9")
        val imageResources = listOf(
            R.drawable.onboarding3_img1,
            R.drawable.onboarding3_img2,
            R.drawable.onboarding3_img3,
            R.drawable.onboarding3_img4,
            R.drawable.onboarding3_img5,
            R.drawable.onboarding3_img6,
            R.drawable.onboarding3_img7,
            R.drawable.onboarding3_img8,
            R.drawable.onboarding3_img9,
        )
        val selectedImageResources = listOf(
            R.drawable.onboarding3_img1_selected,
            R.drawable.onboarding3_img2_selected,
            R.drawable.onboarding3_img3_selected,
            R.drawable.onboarding3_img4_selected,
            R.drawable.onboarding3_img5_selected,
            R.drawable.onboarding3_img6_selected,
            R.drawable.onboarding3_img7_selected,
            R.drawable.onboarding3_img8_selected,
            R.drawable.onboarding3_img9_selected,

            )

        imageViews.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                val imageName = imageNames[index]
                if (selectedImages.contains(imageName)) {
                    selectedImages.remove(imageName)
                    imageView.setImageResource(imageResources[index])
                } else {
                    if (selectedImages.size < 3) {
                        selectedImages.add(imageName)
                        imageView.setImageResource(selectedImageResources[index])
                    } else {
                        // 선택할 수 있는 이미지 수가 초과되었음을 사용자에게 알림
                        Toast.makeText(context, "최대 3개의 이미지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                updateNextButtonState()
            }
        }
    }

    private fun updateNextButtonState() {
        val nextButton = binding.onboarding3Next
        if (selectedImages.isNotEmpty()) {
            nextButton.setImageResource(R.drawable.next_button_on) // 활성화된 상태의 이미지 리소스
            nextButton.isClickable = true
        } else {
            nextButton.setImageResource(R.drawable.next_button_off) // 비활성화된 상태의 이미지 리소스
            nextButton.isClickable = false
        }
    }
}