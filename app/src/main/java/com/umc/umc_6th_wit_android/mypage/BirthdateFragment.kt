package com.umc.umc_6th_wit_android.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentBirthdateBinding

class BirthdateFragment : Fragment(), DatePickerDialogInterface {

    private lateinit var binding: FragmentBirthdateBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBirthdateBinding.inflate(inflater, container, false)

        // 초기값 설정
        sharedViewModel.birthdate.observe(viewLifecycleOwner) { birthdate ->
            binding.birthdateBirthTv.text = birthdate
        }

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

    private fun saveBirthdateAndNavigate() {
        val birthdate = binding.birthdateBirthTv.text.toString()
        sharedViewModel.setBirthdate(birthdate)

        parentFragmentManager.popBackStack()
    }
}
