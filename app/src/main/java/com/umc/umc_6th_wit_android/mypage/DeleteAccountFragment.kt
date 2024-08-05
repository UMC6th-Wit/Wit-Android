package com.umc.umc_6th_wit_android.mypage

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentDeleteAccountBinding

class DeleteAccountFragment : Fragment() {

    private lateinit var binding: FragmentDeleteAccountBinding
    private lateinit var radioGroup: RadioGroup
    private lateinit var etcEditText: EditText
    private lateinit var keepButton: Button
    private lateinit var deleteButton: Button
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

        setupListeners()
        return binding.root
    }

    private fun setupListeners() {
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
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
                moveToMypageFragment()
            }
        }

        keepButton.setOnClickListener {
            // '계속 사용하기' 버튼 클릭 시 동작 추가
            moveToMypageFragment()
        }

        // 뒤로 가기 버튼 클릭 리스너 설정
        binding.DeleteAccountBackIv.setOnClickListener {
            parentFragmentManager.popBackStack()
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
        if (!s.isNullOrEmpty() && s.length > 0) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        // 프래그먼트가 사라질 때 네비게이션 바 다시 표시
        (activity as? MainActivity)?.setBottomNavigationViewVisibility(true)
    }
}
