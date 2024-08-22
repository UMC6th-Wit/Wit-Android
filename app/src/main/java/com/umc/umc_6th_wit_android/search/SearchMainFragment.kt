package com.umc.umc_6th_wit_android.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.umc_6th_wit_android.MainActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.FragmentSearchMainBinding

class SearchMainFragment  : Fragment(), SearchMainView {
    lateinit var binding: FragmentSearchMainBinding
    private var items = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as? MainActivity)?.setBottomNavigationViewVisibility(true) // main_bnv 보이기
        binding = FragmentSearchMainBinding.inflate(inflater, container, false)

        binding.removeTv.setOnClickListener {
            binding.recentCl.visibility = View.GONE
            deleteRecentSearches("")
            //전체 최근 검색어 삭제 api 호출
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
    private fun initRecentSearchRV(result: List<String>) {
/*        //test data
        items.add("이브 진통제")
        items.add("이브 진통제")
        items.add("이브 진통제")
        items.add("이브 진통제")*/

        //api data
        items = ArrayList(result)


        val adapter = SearchRVAdapter(items)
        adapter.setOnItemClickListener(object : SearchRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                changedFragment(items[position])
            }
        })
        adapter.setOnDeleteClickListener(object : SearchRVAdapter.OnDeleteClickListener{
            override fun onDeleteClick(view: View, position: Int) {
                Log.d("keyword", items[position])
                deleteRecentSearches(items[position])
                getSearch()
            }

        })
        binding.searchRv.adapter = adapter
        binding.searchRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
    }
    private fun initPopularSearch(result: List<String>) {
        //임의 data
//        val popular_tags: List<String> = mutableListOf("초콜릿", "향수", "립스틱", "팔찌", "비누", "커피", "마스크팩","비타민","젤리","과자")
//
        // 인기 태그를 텍스트 뷰에 설정
        val nameViews = listOf(binding.name1, binding.name2, binding.name3, binding.name4, binding.name5,
            binding.name6, binding.name7, binding.name8, binding.name9, binding.name10)

        for (i in result.indices) {
            if (i < nameViews.size) {
                nameViews[i].text = result[i]
                nameViews[i].setOnClickListener {
                    changedFragment(result[i])
                }
            }
        }
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

        binding.etSearch.setText(searchQuery)
        if(btn.length > 0){
            // 텍스트 끝에 커서 위치 설정 및 포커스 설정
            binding.etSearch.post {
                binding.etSearch.setSelection(searchQuery.length)
                binding.etSearch.requestFocus()

                // 키보드 표시
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
            }
        }
        // 뒤로가기 버튼 처리 search->home
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // MainActivity의 selectHomeFragment() 호출
                (activity as? MainActivity)?.selectHomeFragment()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getSearch()
    }
    private fun getSearch() {
        val searchService = SearchService(requireContext())
        searchService.setSearchMainView(this)

        searchService.getRecentSearches()//임의
        searchService.getPopularSearches()
    }
    private fun deleteRecentSearches(keyWord : String) {
        val searchService = SearchService(requireContext())
        searchService.setSearchMainView(this)

        searchService.deleteRecentSearches(keyWord!!)//임시 유저 아이디
    }
    override fun onGetRecentSearchSuccess(code: String, result: List<String>) {
        Log.d("RECENT-SEARCH-SUCCESS", code + result)
        initRecentSearchRV(result)
        if(result.size === 0){
            binding.recentCl.visibility = View.GONE
        }
    }

    override fun onGetRecentSearchFailure(code: String, message: String) {
        Log.d("RECENT-SEARCH-FAILURE", code)
    }

    override fun onDeleteRecentSearchSuccess(code: String, message: String) {
        Log.d("RECENT-DELETE-SUCCESS", code + message)
    }

    override fun onDeleteRecentSearchFailure(code: String, message: String) {
        Log.d("RECENT-DELETE-FAILURE", code + message)
    }

    override fun onGetPopularSearchSuccess(code: String, result: List<String>) {
        Log.d("POPULAR-SEARCH-SUCCESS", code)
        initPopularSearch(result)
    }

    override fun onGetPopularSearchFailure(code: String, message: String) {
        Log.d("POPULAR-SEARCH-FAILURE", code)
    }
}
