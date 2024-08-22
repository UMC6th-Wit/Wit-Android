package com.umc.umc_6th_wit_android.onboarding

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentOnboarding1Binding

class OnboardingFragment1 : Fragment() {
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: FragmentOnboarding1Binding
    private val sharedViewModel: SharedOnboardingViewModel by activityViewModels()
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
            if (nextButton.isClickable && selectedImages.size in 1..2) {
                // 선택한 정보 백엔드에 전송 처리 부분
                Log.d("OnboardingFragment1","selectedImages: $selectedImages")
                sharedViewModel.souvenirImages.value = selectedImages.toMutableList()
                val currentItem = viewPager.currentItem
                viewPager.currentItem = currentItem + 1
            }
        }

        val imageViews = listOf(
            binding.onboarding1Img1,
            binding.onboarding1Img2,
            binding.onboarding1Img3,
            binding.onboarding1Img4
        )

        val imageNames = listOf("type1", "type2", "type3", "type4")
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
                    // 선택 해제
                    selectedImages.remove(imageName)
                    imageView.setImageResource(imageResources[index])
                } else {
                    if (selectedImages.size < 2) {
                        // 이미지 선택
                        selectedImages.add(imageName)
                        imageView.setImageResource(selectedImageResources[index])
                    } else {
                        // 선택할 수 있는 이미지 수가 초과되었음을 사용자에게 알림
                        Toast.makeText(context, "최대 2개의 이미지만 선택할 수 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                updateNextButtonState()
            }
        }
    }

    private fun updateNextButtonState() {
        val nextButton = binding.onboarding1Next
        if (selectedImages.size in 1..2) {
            nextButton.setImageResource(R.drawable.next_button_on) // 활성화된 상태의 이미지 리소스
            nextButton.isClickable = true
        } else {
            nextButton.setImageResource(R.drawable.next_button_off) // 비활성화된 상태의 이미지 리소스
            nextButton.isClickable = false
        }
    }
}
