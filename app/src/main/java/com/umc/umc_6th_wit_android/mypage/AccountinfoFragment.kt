package com.umc.umc_6th_wit_android.mypage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentAccountinfoBinding
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.network.TokenRetrofitManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountinfoFragment : Fragment() {

    private lateinit var binding: FragmentAccountinfoBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    // 갤러리에서 이미지 선택 후 결과 처리
    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                navigateToCropActivity(uri)
            }
        }
    }

    // 크롭된 이미지 결과 처리
    private val cropActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                binding.accountinfoProfilIv.setImageURI(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAccountinfoBinding.inflate(inflater, container, false)

        // 유저 정보 조회


        // UI 초기화
        setupUI()

        return binding.root
    }

    // SharedViewModel을 업데이트하는 메서드
    private fun updateSharedViewModel(userResult: UserResult) {
        sharedViewModel.setName(userResult.username)
        sharedViewModel.setNickname(userResult.usernickname)
        sharedViewModel.setBirthdate(userResult.birth)
        sharedViewModel.setGender(userResult.gender)

        // UI 업데이트
        bindUserInfoToUI(userResult)
    }

    // 유저 정보를 UI에 반영하는 메서드
    private fun bindUserInfoToUI(userResult: UserResult?) {
        userResult?.let {
            // 이름, 닉네임 설정
            binding.accountinfoNameEt.setText(it.username)
            binding.accountinfoNicknameEt.setText(it.usernickname)

            // 성별 설정
            if (it.gender == "male") {
                binding.accountinfoMaleRb.isChecked = true
            } else if (it.gender == "female") {
                binding.accountinfoFemaleRb.isChecked = true
            }

            // 생년월일 설정
            binding.accountinfoBirthTv.text = it.birth
        }
    }

    // UI 관련 초기화 로직
    private fun setupUI() {
        // 이름 수정
        binding.accountinfoNameEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                sharedViewModel.setName(v.text.toString())
                hideKeyboardAndClearFocus(binding.accountinfoNameEt)
                true
            } else {
                false
            }
        }

        // 닉네임 수정
        binding.accountinfoNicknameEt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                sharedViewModel.setNickname(v.text.toString())
                hideKeyboardAndClearFocus(binding.accountinfoNicknameEt)
                true
            } else {
                false
            }
        }

        // 뒤로 가기 버튼 클릭 리스너 설정
        binding.accountinfoBackIv.setOnClickListener {
            navigateToMypage()
        }

        // 생년월일 수정 버튼
        binding.accountinfoBirthLinear.setOnClickListener {
            val birthdateFragment = BirthdateFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_frm, birthdateFragment)
                .addToBackStack(null)
                .commit()
        }

        // 프로필 사진 변경 버튼
        binding.accountinfoBox.setOnClickListener {
            openGallery()
        }
        binding.accountinfoProfilIv.scaleType = ImageView.ScaleType.CENTER_INSIDE
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun navigateToCropActivity(uri: Uri) {
        val intent = Intent(requireContext(), CropActivity::class.java).apply {
            putExtra("imageUri", uri)
        }
        cropActivityLauncher.launch(intent)
    }

    private fun hideKeyboardAndClearFocus(view: View) {
        val imm = activity?.getSystemService(InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }

    private fun navigateToMypage() {
        parentFragmentManager.popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 프래그먼트가 사라질 때 네비게이션 바 다시 표시
        (activity as? MainActivity)?.setBottomNavigationViewVisibility(true)
    }
}
