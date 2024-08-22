package com.umc.umc_6th_wit_android.mypage

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentBirthdateBinding
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.network.TokenRetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BirthdateFragment : Fragment(), DatePickerDialogInterface {

    private lateinit var binding: FragmentBirthdateBinding
    private lateinit var tokenManager: TokenManager
    private var userInfo: UserResult? = null // 서버에서 받은 유저 정보 저장

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBirthdateBinding.inflate(inflater, container, false)
        (activity as? MainActivity)?.setBottomNavigationViewVisibility(false)

        // TokenManager 초기화
        tokenManager = TokenManager(requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE))

        // 서버에서 생년월일을 불러옴
        fetchUserInfo()
        Log.d("BirthdateFragment","fechUserInfo() 실행")


        binding.birthdateBirthTv.setOnClickListener {
            showDatePickerDialog()
        }

        // 뒤로 가기 버튼 클릭 리스너 설정
        binding.birthdateBackIv.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 저장 버튼 클릭 리스너 설정
        binding.birthdateSaveBtn.setOnClickListener {
            saveBirthdateAndNavigate()
        }

        return binding.root
    }

    // 서버에서 유저 정보를 가져오는 함수
    private fun fetchUserInfo() {
        val accessToken = tokenManager.getAccessToken()

        if (accessToken != null) {
            val retrofit = TokenRetrofitManager(requireContext())
            val userService = retrofit.create(UserService::class.java)

            val call = userService.getUserInfo("Bearer $accessToken")
            call.enqueue(object : Callback<UserInfoResponse> {
                override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        val userInfoResult = response.body()?.result
                        userInfoResult?.let {
                            userInfo = it // 유저 정보 저장
                            val birthdate = formatBirthdate(it.birth)
                            binding.birthdateBirthTv.text = birthdate // UI에 반영
                            Log.d("BirthdateFragment","birthdate: $birthdate")
                        }
                    } else {
                        Log.d("BirthdateFragment", "Error: ${response.body()?.message}")
                    }
                }

                override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                    Log.d("BirthdateFragment", "API 호출 실패: ${t.message}")
                }
            })
        } else {
            Log.d("BirthdateFragment", "액세스 토큰이 없습니다.")
        }
    }

    private fun showDatePickerDialog() {
        val birthdateText = binding.birthdateBirthTv.text.toString()
        val parts = birthdateText.split(".")
        val initialYear = parts.getOrNull(0)?.toInt() ?: 2000
        val initialMonth = parts.getOrNull(1)?.toInt() ?: 1
        val initialDay = parts.getOrNull(2)?.toInt() ?: 1

        val datePickerDialog = DatePickerDialog(this, initialYear, initialMonth, initialDay)
        datePickerDialog.show(parentFragmentManager, "datePickerDialog")
    }

    override fun onClickDoneButton(year: Int, month: Int, day: Int) {
        val birthdate = "$year.${String.format("%02d", month)}.${String.format("%02d", day)}"
        binding.birthdateBirthTv.text = birthdate
    }

    // 생년월일 수정 성공 시 결과 전달
    private fun saveBirthdateAndNavigate() {
        val accessToken = tokenManager.getAccessToken()

        if (accessToken != null && userInfo != null) {
            val birthdate = binding.birthdateBirthTv.text.toString().replace(".", "")
            val username = userInfo!!.username
            val usernickname = userInfo!!.usernickname
            val gender = userInfo!!.gender
            val age = userInfo!!.age

            val userInfoUpdateRequest = UserInfoUpdateRequest(username, usernickname, gender, age, birthdate)

            val retrofit = TokenRetrofitManager(requireContext())
            val userService = retrofit.create(UserService::class.java)

            val call = userService.updateUserInfo("Bearer $accessToken", userInfoUpdateRequest)
            call.enqueue(object : Callback<UserInfoResponse> {
                override fun onResponse(call: Call<UserInfoResponse>, response: Response<UserInfoResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        Log.d("BirthdateFragment", "유저 정보 수정 성공")
                        Log.d("BirthdateFragment", "bithdate: $birthdate")

                        // 생년월일 수정 성공 후 결과 전달
                        setFragmentResult("birthdateUpdate", Bundle().apply {
                            putString("updatedBirthdate", birthdate)
                        })

                        parentFragmentManager.popBackStack()
                    } else {
                        Log.d("BirthdateFragment", "유저 정보 수정 실패: ${response.body()?.message}")
                    }
                }

                override fun onFailure(call: Call<UserInfoResponse>, t: Throwable) {
                    Log.d("BirthdateFragment", "유저 정보 수정 API 호출 실패: ${t.message}")
                }
            })
        } else {
            Log.d("BirthdateFragment", "유저 정보가 없습니다.")
        }
    }


    // 생년월일 포맷을 yyyy.MM.dd로 변환하는 함수
    private fun formatBirthdate(birth: String): String {
        val year = birth.substring(0, 4).padStart(4, '0')
        val month = birth.substring(4, 6).padStart(2, '0')
        val day = birth.substring(6, 8).padStart(2, '0')
        return "$year.$month.$day"
    }



}
