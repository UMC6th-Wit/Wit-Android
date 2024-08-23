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
import com.umc.umc_6th_wit_android.data.remote.search.SearchResult
import com.umc.umc_6th_wit_android.data.remote.search.SearchService
import com.umc.umc_6th_wit_android.data.remote.search.Souvenir
import com.umc.umc_6th_wit_android.databinding.FragmentSearchRsltBinding
import com.umc.umc_6th_wit_android.home.CustomRVAdapter
import com.umc.umc_6th_wit_android.product.ProductDetailActivity
import com.umc.umc_6th_wit_android.wish.WishAdapter


class SearchRsltFragment : Fragment(), SearchRsltView {
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

        /*// 임의 test data 추가
        items.add(Souvenir(1,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f))
        items.add(Souvenir(2,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f))
        items.add(Souvenir(3,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f))
        items.add(Souvenir(4,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f))
        items.add(Souvenir(5,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f))
        items.add(Souvenir(6,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f))
        items.add(Souvenir(7,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f))
        items.add(Souvenir(8,  "포테이토 칩스 우스시오 아지", 367, 3151, "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg", 10, 4.40f))
*/
        // result.souvenirs 데이터를 사용하려면 아래 주석을 해제합니다.
//         items = ArrayList(result.souvenirs)
        adapter = CustomRVAdapter(items){ currentCursor, limit ->
            getSearches(currentCursor, limit)
        }
        adapter.setOnItemClickListener(object : CustomRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(activity, ProductDetailActivity::class.java)
                startActivity(intent)
//                changeActivity(items[position])
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
}