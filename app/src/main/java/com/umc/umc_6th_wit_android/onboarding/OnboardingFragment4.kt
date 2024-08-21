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
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.databinding.FragmentOnboarding4Binding

class OnboardingFragment4 : Fragment() {
    private lateinit var binding:FragmentOnboarding4Binding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboarding4Binding.inflate(inflater, container, false)
        sharedPreferences = requireContext().getSharedPreferences("OnboardingPrefs", Context.MODE_PRIVATE)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.onboarding4Start.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.putBoolean("onboardingCompleted", true)
            editor.apply() // 온보딩 완료 상태를 저장
            Log.d("Onboarding","온보딩완료 상태 저장")

            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish() // 현재 프래그먼트를 포함한 액티비티 종료
        }
    }
}