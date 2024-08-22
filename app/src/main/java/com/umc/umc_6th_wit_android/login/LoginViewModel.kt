package com.umc.umc_6th_wit_android.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.umc.umc_6th_wit_android.data.local.LoginDto
import com.umc.umc_6th_wit_android.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val _loginResponse = MutableLiveData<LoginDto>()
    val loginResponse: LiveData<LoginDto>
        get() = _loginResponse

    fun fetchNaverLoginPage() {
        val apiService = getRetrofit().create(LoginApi::class.java)
        apiService.getNaverLoginPage().enqueue(object : Callback<LoginDto> {
            override fun onResponse(call: Call<LoginDto>, response: Response<LoginDto>) {
                if (response.isSuccessful) {
                    _loginResponse.postValue(response.body())
                } else {
                    // Handle error case
                    _loginResponse.postValue(LoginDto(false, "Error", "Failed to load page", null))
                }
            }

            override fun onFailure(call: Call<LoginDto>, t: Throwable) {
                // Handle network errors
                _loginResponse.postValue(LoginDto(false, "Error", t.message ?: "Unknown error", null))
            }
        })
    }
}
