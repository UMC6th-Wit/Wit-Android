package com.umc.umc_6th_wit_android.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.remote.home.HomeService
import com.umc.umc_6th_wit_android.data.remote.search.SearchResult
import com.umc.umc_6th_wit_android.data.remote.search.SearchService
import com.umc.umc_6th_wit_android.data.remote.search.Souvenir
import com.umc.umc_6th_wit_android.databinding.FragmentSearchRsltBinding
import com.umc.umc_6th_wit_android.home.CustomRVAdapter
import com.umc.umc_6th_wit_android.product.ProductDetailActivity
import com.umc.umc_6th_wit_android.wish.HeartView
import com.umc.umc_6th_wit_android.wish.WishAdapter


class SearchRsltFragment : Fragment(), SearchRsltView, HeartView {
    lateinit var binding: FragmentSearchRsltBinding
    private var searchQuery: String? = null
    private lateinit var adapter: CustomRVAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchRsltBinding.inflate(layoutInflater)

        // arguments에서 검색어 데이터 가져오기
        searchQuery = arguments?.getString("searchQuery")
        binding.etSearch.setText(searchQuery)


        binding.ivReset.setOnClickListener {
            changeFragment("","ivReset")
        }

        binding.tvCancel.setOnClickListener {
            changeFragment("","")
        }


        binding.etSearch.setOnTouchListener { v, event ->
            changeFragment(searchQuery, "etSearch")

            // true를 반환하여 이벤트를 소비하고, false를 반환하여 이벤트를 계속 처리할지 결정
            true
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // OnBackPressedCallback을 추가하여 뒤로가기 버튼을 처리
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                changeFragment("", "")
            }
        })
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

    override fun onResume() {
        super.onResume()
        initRecyclerView()
        getSearches(0, 20)//화면이 생성될때마다 = 검색할때마다 요청
    }
    private fun getSearches(cursor: Int?, limit: Int?) {
        val searchService = SearchService(requireContext())
        searchService.setSearchRsltView(this)

        searchService.getSearches(searchQuery, cursor, limit)
    }
    private fun initRecyclerView() {
        // ArrayList 초기화
        var items: ArrayList<Souvenir> = ArrayList()

        adapter = CustomRVAdapter(items,
            { currentCursor, limit -> getSearches(currentCursor, limit) },
            { id -> addCart(id) },  // addCart 함수 람다식으로 전달
            { id -> delCart(id) } )
        adapter.setOnItemClickListener(object : CustomRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(activity, ProductDetailActivity::class.java)
                startActivity(intent)
            }
        })
        binding.rsltRv.adapter = adapter
        binding.rsltRv.layoutManager  = GridLayoutManager(context, 2)
    }
    override fun onGetSearchSuccess(code: String, result: SearchResult) {
        Log.d("SEARCH-SUCCESS", code + result.souvenirs)
        binding.totalTv.text = "총 ${result.total}개"
        adapter.addItems(ArrayList(result.souvenirs))
        adapter.currentCursor = result.nextCursor
    }

    override fun onGetSearchFailure(code: String, message: String) {
        Log.d("SEARCH-FAILURE", code)
    }
    private fun addCart(id:Int) {
        val homeService = HomeService(requireContext())
        homeService.setHeartView(this)
        homeService.addCart(id)
    }
    private fun delCart(id:Int) {
        val homeService = HomeService(requireContext())
        homeService.setHeartView(this)
        homeService.delCart(id)
    }

    override fun onAddWishSuccess(code: String, result: String) {
        Log.d("ADDCART_SUCCESS", code)
    }

    override fun onAddWishFailure(code: String, message: String) {
        Log.d("ADDCART_FAILURE", code)
    }

    override fun onDeleteWishSuccess(code: String, result: String) {
        Log.d("DELCART_SUCCESS", code)
    }

    override fun onDeleteWishFailure(code: String, message: String) {
        Log.d("DELCART_FAILURE", code)
    }

}