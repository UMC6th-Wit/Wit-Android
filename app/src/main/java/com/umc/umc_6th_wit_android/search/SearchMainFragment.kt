package com.umc.umc_6th_wit_android.search

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayout
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentSearchBinding
import com.umc.umc_6th_wit_android.databinding.FragmentSearchMainBinding
import com.umc.umc_6th_wit_android.home.HomeCustomRVAdapter
import com.umc.umc_6th_wit_android.home.ProductDetailActivity

class SearchMainFragment  : Fragment() {
    lateinit var binding: FragmentSearchMainBinding
    private val items = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchMainBinding.inflate(inflater, container, false)

        //test data
        items.add("이브 진통제")
        items.add("이브 진통제")
        items.add("이브 진통제")
        items.add("이브 진통제")

        val adapter = SearchRVAdapter(items)
        adapter.setOnItemClickListener(object : SearchRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                changedFragment(items[position])
            }
        })
        binding.searchRv.adapter = adapter
        binding.searchRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

        binding.removeTv.setOnClickListener {
            binding.recentCl.visibility = View.GONE
        }
        
        val popular_tags: List<String> = mutableListOf("초콜릿", "향수", "립스틱", "팔찌", "비누", "커피", "마스크팩","비타민","젤리","과자")

        // 인기 태그를 텍스트 뷰에 설정
        val nameViews = listOf(binding.name1, binding.name2, binding.name3, binding.name4, binding.name5,
            binding.name6, binding.name7, binding.name8, binding.name9, binding.name10)

        for (i in popular_tags.indices) {
            if (i < nameViews.size) {
                nameViews[i].text = popular_tags[i]
                nameViews[i].setOnClickListener {
                    changedFragment(popular_tags[i])
                }
            }
        }


        //검색창 엔터키 눌렀을 때
        binding.etSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                // 사용자가 엔터를 눌렀을 때의 동작
                val searchQuery = textView.text.toString()
                changedFragment(searchQuery)

                true
            } else {
                false
            }
        }
        binding.ivSearch.setOnClickListener {
            val searchQuery = binding.etSearch.text.toString()
            changedFragment(searchQuery)
        }

        binding.ivReset.setOnClickListener {
            binding.etSearch.setText("")
        }

        //추천 검색어
        val buttons = listOf(binding.button1, binding.button2, binding.button3, binding.button4, binding.button5,
            binding.button6, binding.button7)
        for (i in buttons.indices) {
            if (i < buttons.size) {
                buttons[i].setOnClickListener {
                    changedFragment(buttons[i].text.toString())
                }
            }
        }

        return binding.root
    }
    private fun changedFragment(searchQuery : String){
//        binding.etSearch.setText(searchQuery)//다시 서치메인으로 돌아올 때 적혀있어야 함.
        // SearchResultFragment로 이동
        val fragment = SearchRsltFragment()
        val bundle = Bundle().apply {
            putString("searchQuery", searchQuery)
        }
        fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(
                R.id.main_frm,
                fragment
            )
            .addToBackStack(null)
            .commit()
    }
    //Fragment가 뷰를 생성할 때마다 실행
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // arguments에서 searchQuery 가져오기
        val searchQuery = arguments?.getString("searchQuery") ?: ""
        val btn = arguments?.getString("btn") ?: ""

        // et_search에 텍스트 설정
        binding.etSearch.setText(searchQuery)
        if(searchQuery.length > 0 || btn.length > 0){
            // 텍스트 끝에 커서 위치 설정 및 포커스 설정
            binding.etSearch.post {
                binding.etSearch.setSelection(searchQuery.length)
                binding.etSearch.requestFocus()

                // 키보드 표시
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

}
