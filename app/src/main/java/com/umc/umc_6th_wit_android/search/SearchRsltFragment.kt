package com.umc.umc_6th_wit_android.search

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.local.CategoryDao
import com.umc.umc_6th_wit_android.data.local.CategoryDto
import com.umc.umc_6th_wit_android.databinding.FragmentSearchRsltBinding
import com.umc.umc_6th_wit_android.home.CustomRVAdapter
import com.umc.umc_6th_wit_android.home.ProductDetailActivity

class SearchRsltFragment : Fragment() {
    lateinit var binding: FragmentSearchRsltBinding
    private var searchQuery: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchRsltBinding.inflate(layoutInflater)

        // arguments에서 검색어 데이터 가져오기
        searchQuery = arguments?.getString("searchQuery")
        binding.etSearch.setText(searchQuery)

        val items = CategoryDao().items

        //test data
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))

        val adapter = CustomRVAdapter(items)
        adapter.setOnItemClickListener(object : CustomRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(activity, ProductDetailActivity::class.java)
                startActivity(intent)
//                changeActivity(items[position])
            }
        })
        binding.rsltRv.adapter = adapter
        binding.rsltRv.layoutManager  = GridLayoutManager(context, 2)


        binding.ivReset.setOnClickListener {
            changeFragment("","ivReset")
        }

        binding.tvCancel.setOnClickListener {
            changeFragment("","")
        }


        binding.etSearch.setOnTouchListener { v, event ->
            changeFragment(searchQuery, "")

            // true를 반환하여 이벤트를 소비하고, false를 반환하여 이벤트를 계속 처리할지 결정
            true
        }
        return binding.root
    }

    private fun changeFragment(searchQuery : String?, btn:String?){
        // 검색 쿼리를 전달할 SearchMainFragment를 생성
        val fragment = SearchMainFragment()

        // 전달할 데이터를 담을 Bundle을 생성
        val bundle = Bundle().apply {
            putString("searchQuery", searchQuery)
            putString("btn", btn)
        }

        // Bundle을 Fragment의 arguments로 설정
        fragment.arguments = bundle

        // FragmentManager를 사용하여 SearchMainFragment로 이동
        parentFragmentManager.beginTransaction()
            .replace(R.id.main_frm, fragment) // 프래그먼트를 교체할 컨테이너의 ID
            .addToBackStack(null) // 백스택에 추가하여 뒤로가기 시 이전 화면으로 돌아감
            .commit()
    }
}