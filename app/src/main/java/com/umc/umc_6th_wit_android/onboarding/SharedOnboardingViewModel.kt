package com.umc.umc_6th_wit_android.onboarding

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedOnboardingViewModel : ViewModel() {
    val souvenirImages = MutableLiveData<MutableList<String>>(mutableListOf())
    val destinationImages = MutableLiveData<MutableList<String>>(mutableListOf())
    val personalityImages = MutableLiveData<MutableList<String>>(mutableListOf())
}
