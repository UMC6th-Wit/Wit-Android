package com.umc.umc_6th_wit_android.mypage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentDeleteAccountBinding
import com.umc.umc_6th_wit_android.login.LoginActivity
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.network.TokenRetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteAccountFragment : Fragment() {

    private lateinit var binding: FragmentDeleteAccountBinding
    private lateinit var radioGroup: RadioGroup
    private lateinit var etcEditText: EditText
    private lateinit var keepButton: Button
    private lateinit var deleteButton: Button
    private lateinit var tokenManager: TokenManager
    private var isDeleteButtonEnabled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDeleteAccountBinding.inflate(inflater, container, false)
        radioGroup = binding.deleteAccountRg
        etcEditText = binding.deleteAccountEtcEd
        keepButton = binding.deleteAccountKeepBtn
        deleteButton = binding.deleteAccountDeleteBtn

        tokenManager = TokenManager(requireContext().getSharedPreferences("auth_prefs", Context.MODE_PRIVATE))

        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            handleRadioButtonSelection(checkedId)
        }

        etcEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                handleEtcEditTextChange(s)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        deleteButton.setOnClickListener {
            if (isDeleteButtonEnabled) {
                performUserWithdrawal()  // 회원탈퇴 API 호출

            }
        }

        keepButton.setOnClickListener {
            moveToMypageFragment()
        }

        // 뒤로 가기 버튼 클릭 리스너 설정
        binding.DeleteAccountBackIv.setOnClickListener {
            moveToMypageFragment()
        }
    }

    private fun performUserWithdrawal() {
        val accessToken = tokenManager.getAccessToken()

        if (accessToken != null) {
            val retrofit = TokenRetrofitManager(requireContext())
            val userService = retrofit.create(UserService::class.java)

            val call = userService.withdrawUser("Bearer $accessToken")
            call.enqueue(object : Callback<WithdrawResponse> {
                override fun onResponse(call: Call<WithdrawResponse>, response: Response<WithdrawResponse>) {
                    if (response.isSuccessful && response.body()?.isSuccess == true) {
                        // 회원탈퇴 성공
                        Log.d("DeleteAccountFragment","회원탈퇴 성공")
                        tokenManager.clearTokens() // 토큰 초기화
                        moveToLoginScreen() // 로그인 화면으로 이동
                    } else {
                        // 에러 처리 (토큰 만료 등)
                        handleError(response.body()?.message)
                    }
                }

                override fun onFailure(call: Call<WithdrawResponse>, t: Throwable) {
                    // 네트워크 실패 처리
                    handleError(t.message)
                }
            })
        }
    }

    private fun handleRadioButtonSelection(checkedId: Int) {
        if (checkedId == R.id.deleteAccount_rb7) {
            etcEditText.visibility = View.VISIBLE
            handleEtcEditTextChange(etcEditText.text)
        } else {
            etcEditText.visibility = View.GONE
            enableDeleteButton()
        }
    }

    private fun handleEtcEditTextChange(s: CharSequence?) {
        if (!s.isNullOrEmpty()) {
            enableDeleteButton()
        } else {
            disableDeleteButton()
        }
    }

    private fun enableDeleteButton() {
        deleteButton.isEnabled = true
        deleteButton.setBackgroundResource(R.drawable.delete_account_button3)
        deleteButton.setTextColor(resources.getColor(R.color.white, null))
        isDeleteButtonEnabled = true
    }

    private fun disableDeleteButton() {
        deleteButton.isEnabled = false
        deleteButton.setBackgroundResource(R.drawable.delete_account_button2)
        deleteButton.setTextColor(resources.getColor(R.color.gray, null))
        isDeleteButtonEnabled = false
    }

    private fun moveToMypageFragment() {
        parentFragmentManager.popBackStack()
    }

    // 회원탈퇴 성공 후 로그인 화면으로 이동하는 로직 추가
    private fun moveToLoginScreen() {
        // 온보딩 정보도 초기화
        val onboardingPrefs = requireContext().getSharedPreferences("OnboardingPrefs", Context.MODE_PRIVATE)
        val onboardingEditor = onboardingPrefs.edit()
        onboardingEditor.clear() // 온보딩 관련 정보 초기화
        onboardingEditor.apply()
        // 토큰과 사용자 정보 지우기
        tokenManager.clearTokens()

        // 로그인 화면으로 이동
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun handleError(message: String?) {
        // 오류 메시지 처리
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 프래그먼트가 사라질 때 네비게이션 바 다시 표시
        (activity as? MainActivity)?.setBottomNavigationViewVisibility(true)
    }
}
