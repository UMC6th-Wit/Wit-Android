package com.umc.umc_6th_wit_android.mypage

import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.umc.umc_6th_wit_android.databinding.BirthDatepickerBinding
import java.lang.reflect.Field
import java.util.*

interface DatePickerDialogInterface {
    fun onClickDoneButton(year: Int, month: Int, day: Int)
}

class DatePickerDialog(
    private val datePickerDialogInterface: DatePickerDialogInterface,
    private val initialYear: Int,
    private val initialMonth: Int,
    private val initialDay: Int
) : BottomSheetDialogFragment() {

    private lateinit var binding: BirthDatepickerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BirthDatepickerBinding.inflate(inflater, container, false)
        val yearPicker = binding.birthYearpicker
        val monthPicker = binding.birthMonthpicker
        val dayPicker = binding.birthDaypicker

        // Done 버튼 눌러서 창 닫기
        binding.saveButtonDatepicker.setOnClickListener {
            // 값 가져오기
            val year = yearPicker.value
            val month = monthPicker.value + 1
            val day = dayPicker.value + 1

            datePickerDialogInterface.onClickDoneButton(year, month, day)
            // 닫기
            dismiss()
        }
        // Done 버튼 눌러서 창 닫기
        binding.cancelButtonDatepicker.setOnClickListener {
            // 닫기
            dismiss()
        }

        //  순환 안되게 막기
        yearPicker.wrapSelectorWheel = false
        monthPicker.wrapSelectorWheel = false
        dayPicker.wrapSelectorWheel = false

        //  editText 설정 해제
        yearPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        monthPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        dayPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        //  최소값 설정
        yearPicker.minValue = 1900
        monthPicker.minValue = 0
        dayPicker.minValue = 0

        //  최대값 설정
        yearPicker.maxValue = Calendar.getInstance().get(Calendar.YEAR)
        monthPicker.maxValue = 11
        dayPicker.maxValue = 30

        //  array 값 넣기
        val yearsArray = Array(yearPicker.maxValue - yearPicker.minValue + 1) { i -> "${1900 + i}년" }
        yearPicker.displayedValues = yearsArray
        monthPicker.displayedValues = Array(12) { "${it + 1}월" }
        dayPicker.displayedValues = Array(31) { "${it + 1}일" }

        // 선택된 기본값 설정
        yearPicker.value = initialYear
        monthPicker.value = initialMonth - 1
        dayPicker.value = initialDay - 1

        // 달과 연도가 변경될 때 일 수 조정
        val adjustDaysInMonth = {
            val year = yearPicker.value
            val month = monthPicker.value + 1
            val maxDay = getMaxDaysInMonth(year, month)
            val currentDay = dayPicker.value + 1
            dayPicker.maxValue = maxDay - 1
            dayPicker.displayedValues = Array(maxDay) { "${it + 1}일" }
            if (currentDay > maxDay) {
                dayPicker.value = maxDay - 1
            } else {
                dayPicker.value = currentDay - 1
            }
        }

        // 리스너 설정
        yearPicker.setOnValueChangedListener { _, _, _ -> adjustDaysInMonth() }
        monthPicker.setOnValueChangedListener { _, _, _ -> adjustDaysInMonth() }

        return binding.root
    }

    private fun getMaxDaysInMonth(year: Int, month: Int): Int {
        return when (month) {
            4, 6, 9, 11 -> 30 // 30일이 있는 달
            2 -> if (isLeapYear(year)) 29 else 28 // 2월은 윤년 여부에 따라 다름
            else -> 31 // 그 외는 31일
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

}
