package com.umc.umc_6th_wit_android.search

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayout
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.local.PopupDto
import com.umc.umc_6th_wit_android.databinding.FragmentSearchBinding
import com.umc.umc_6th_wit_android.home.HomePopupRVAdapter

class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    private val items = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        //test data
        items.add("이브 진통제")
        items.add("이브 진통제")
        items.add("이브 진통제")
        items.add("이브 진통제")

        val adapter = SearchRVAdapter(items)

        binding.searchRv.adapter = adapter
        binding.searchRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)


        // 버튼을 동적으로 추가
        for (i in 1..7) {
            addButton(binding.flexboxLayout, "버튼 $i")
        }


        return binding.root
    }
    private fun addButton(flexboxLayout: FlexboxLayout, buttonText: String) {
        val button = Button(requireActivity()).apply {
            text = buttonText
            layoutParams = FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
            ).apply {
//                setMargins(8, 8, 8, 8) // 버튼의 마진 설정
            }
            setTextAppearance(R.style.SearchCustomButton) // 스타일 적용
        }


        // 버튼 클릭 이벤트
        button.setOnClickListener {
            Toast.makeText(requireActivity(), buttonText, Toast.LENGTH_SHORT).show()
        }

        flexboxLayout.addView(button)
    }
}
