package com.umc.umc_6th_wit_android.onboarding

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.databinding.FragmentOnboarding4Binding
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.network.TokenRetrofitManager
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class OnboardingFragment4 : Fragment() {
    private lateinit var binding:FragmentOnboarding4Binding
    private lateinit var sharedPreferences: SharedPreferences
    private val sharedViewModel: SharedOnboardingViewModel by activityViewModels()
    private lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboarding4Binding.inflate(inflater, container, false)

        // TokenManager 초기화
        tokenManager = TokenManager(requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE))

        sharedPreferences = requireContext().getSharedPreferences("OnboardingPrefs", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.onboarding4Start.setOnClickListener {
            val souvenirImages = sharedViewModel.souvenirImages.value ?: listOf()
            val destinationImages = sharedViewModel.destinationImages.value ?: listOf()
            val personalityImages = sharedViewModel.personalityImages.value ?: listOf()

            // 서버로 POST 요청 보내기
            sendOnboardingData(souvenirImages, destinationImages, personalityImages)

            val editor = sharedPreferences.edit()
            editor.putBoolean("onboardingCompleted", true)
            editor.apply() // 온보딩 완료 상태를 저장
            Log.d("Onboarding","온보딩완료 상태 저장")


            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // 현재 프래그먼트를 포함한 액티비티 종료
        }
    }

    private fun sendOnboardingData(souvenir: List<String>, destination: List<String>, personality: List<String>) {
        val accessToken = tokenManager.getAccessToken()

        if (accessToken != null) {
            val onboardingRequest = OnboardingRequest(souvenir, destination, personality)
            val retrofit = TokenRetrofitManager(requireContext())
            val onboardingService = retrofit.create(OnboardingService::class.java)

            onboardingService.sendOnboardingData("Bearer $accessToken", onboardingRequest)
                .enqueue(object : Callback<OnboardingResponse> {
                    override fun onResponse(call: Call<OnboardingResponse>, response: Response<OnboardingResponse>) {
                        if (response.isSuccessful && response.body()?.isSuccess == true) {
                            // 성공 시 처리 로직
                            Log.d("OnboardingFragment4", "온보딩 성공")
                        } else {
                            // 실패 시 처리 로직
                            Log.d("OnboardingFragment4", "온보딩 실패: ${response.body()?.message}")
                        }
                    }

                    override fun onFailure(call: Call<OnboardingResponse>, t: Throwable) {
                        Log.d("OnboardingFragment4", "온보딩 API 호출 실패: ${t.message}")
                    }
                })
        } else {
            Log.d("OnboardingFragment4", "액세스 토큰이 없습니다.")
        }
    }
}