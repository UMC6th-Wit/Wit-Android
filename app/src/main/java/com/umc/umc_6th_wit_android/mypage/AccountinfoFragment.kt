package com.umc.umc_6th_wit_android.mypage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentAccountinfoBinding

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

        // 초기값 설정
        binding.accountinfoNameEt.setText(sharedViewModel.name.value)
        binding.accountinfoNicknameEt.setText(sharedViewModel.nickname.value)

        // 초기값 설정
        sharedViewModel.birthdate.observe(viewLifecycleOwner) { birthdate ->
            binding.accountinfoBirthTv.text = birthdate
        }

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

        //생년월일 수정 버튼
        binding.accountinfoBirthLinear.setOnClickListener {
            // AccountinfoFragment로 전환
            val birthdateFragment = BirthdateFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.add(R.id.main_frm, birthdateFragment)
            transaction.addToBackStack(null)  // 백스택에 추가하여 뒤로 가기 버튼으로 돌아올 수 있게 함
            transaction.commit()
        }

        // 프로필 사진 변경 버튼
        binding.accountinfoBox.setOnClickListener {
            openGallery()
        }

        return binding.root
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
        view.clearFocus() // EditText focus 해제
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
