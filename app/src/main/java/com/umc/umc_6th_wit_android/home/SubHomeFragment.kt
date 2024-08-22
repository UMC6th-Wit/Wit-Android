package com.umc.umc_6th_wit_android.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.umc.umc_6th_wit_android.CosmeticActivity
import com.umc.umc_6th_wit_android.DailyActivity
import com.umc.umc_6th_wit_android.FoodActivity
import com.umc.umc_6th_wit_android.MedicineActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.local.PersonalDao
import com.umc.umc_6th_wit_android.data.remote.home.HomeResult
import com.umc.umc_6th_wit_android.data.remote.home.HomeService
import com.umc.umc_6th_wit_android.data.remote.home.Product
import com.umc.umc_6th_wit_android.data.remote.home.ProductVer2
import com.umc.umc_6th_wit_android.data.remote.search.SearchService
import com.umc.umc_6th_wit_android.home.notice.NoticeActivity
import com.umc.umc_6th_wit_android.databinding.FragmentSubhomeBinding
import com.umc.umc_6th_wit_android.product.ProductDetailActivity

class SubHomeFragment : Fragment(), SubHomeView{
    lateinit var binding: FragmentSubhomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSubhomeBinding.inflate(inflater, container, false)

        binding.food.setOnClickListener {
            val intent = Intent(context, FoodActivity::class.java)
            startActivity(intent)
        }
        binding.cosmetic.setOnClickListener {
            val intent = Intent(context, CosmeticActivity::class.java)
            startActivity(intent)
        }
        binding.medicine.setOnClickListener {
            val intent = Intent(context, MedicineActivity::class.java)
            startActivity(intent)
        }
        binding.daily.setOnClickListener {
            val intent = Intent(context, DailyActivity::class.java)
            startActivity(intent)
        }




        //맞춤 추천템 더보기
        binding.customBtn.setOnClickListener {
            val intent = Intent(activity, CustomActivity::class.java)
            startActivity(intent)
        }
        //랭킹 더보기
        binding.rankingBtn.setOnClickListener {
            val parentFragment = parentFragment
            if (parentFragment is HomeFragment) {
                parentFragment.navigateToRanking()
            }
        }

        //맛도리 추천템 더보기
        binding.foodBtn.setOnClickListener {
            val intent = Intent(activity, BestFoodActivity::class.java)
            startActivity(intent)
        }
        //공지 더보기
        binding.noticeBtn.setOnClickListener{
            val intent = Intent(activity, NoticeActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }


//    private fun changeActivity(personal : PersonalDto) {
//        val intent = Intent(activity, DetailActivity::class.java)
//        intent.putExtra("dto", personal)   // 클릭한 RecyclerView 항목 위치의 dto 전달
//        startActivity(intent)
//    }
    private fun tabItemMargin(mTabLayout : TabLayout){
        for (i in 0 until mTabLayout.getTabCount()) {
            val tab = (mTabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            p.setMargins(0, 0, 25,0)
            tab.setPadding(40,0,40,0)
            tab.requestLayout()
        }
    }
    override fun onResume() {
        super.onResume()
        getHomeProducts()
    }
    private fun getHomeProducts() {
        val homeService = HomeService(requireContext())
        homeService.setSubHomeView(this)

        homeService.getHomeProducts()
    }
    override fun onGetHomeSuccess(code: String, result: HomeResult) {
        Log.d("HOME-SUCCESS", code)
        binding.recommendTitle.text = "${result.user.username}님을 위한 추천템"
        initPersonalRV(result.recommendations)
        initCategoryVp(result.popularProducts)
        initFoodRV(result.nyamRecommendations)
        //공지사항
        val notices = result.notices ?: emptyList() // notices가 null인 경우 빈 리스트로 처리

        // 첫 번째 공지
        binding.noticeTag.text = "[${notices.getOrNull(0)?.title}]" ?: ""
        binding.noticeContent.text = notices.getOrNull(0)?.content ?: ""

        // 두 번째 공지
        binding.noticeTag2.text = "[${notices.getOrNull(1)?.title}]" ?: ""
        binding.noticeContent2.text = notices.getOrNull(1)?.content ?: ""

        // 세 번째 공지
        binding.noticeTag3.text = "[${notices.getOrNull(2)?.title}]" ?: ""
        binding.noticeContent3.text = notices.getOrNull(2)?.content ?: ""
    }

    override fun onGetHomeFailure(code: String, message: String) {
        Log.d("HOME-FAILURE", code)
    }

    private fun initPersonalRV(result: List<Product>){
//        val items = PersonalDao().items
        Log.d("initPersonalRV", result.toString())

        val items = ArrayList(result)
        val adapter = HomeCustomRVAdapter(items, "personal")
        adapter.setOnItemClickListener(object : HomeCustomRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(activity, ProductDetailActivity::class.java)
                startActivity(intent)
//                changeActivity(items[position])
            }
        })

        binding.personalRv.adapter = adapter
        binding.personalRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)


    }
    private fun initFoodRV(result: List<Product>){
        val items = ArrayList(result)

        val foodAdapter = HomeCustomRVAdapter(items, "food")
        foodAdapter.setOnItemClickListener(object : HomeCustomRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(activity, ProductDetailActivity::class.java)
                startActivity(intent)
//                changeActivity(items[position])
            }
        })
        binding.foodRv.adapter = foodAdapter
        binding.foodRv.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
    }
    private fun initCategoryVp(result: Map<String, List<ProductVer2>>){
        val category : Array<String> = resources.getStringArray(R.array.CATEGORY)

        val rankingAdapter = HomeCategoryVPAdapter(this, result)
        binding.templateCategoryVp.adapter = rankingAdapter

        TabLayoutMediator(binding.templateSelectCategoryTl, binding.templateCategoryVp){
                tab, position->
            tab.text= category[position]  //포지션에 따른 텍스트
        }.attach()  //탭레이아웃과 뷰페이저를 붙여주는 기능

        tabItemMargin(binding.templateSelectCategoryTl)
    }
}