package com.umc.umc_6th_wit_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.umc_6th_wit_android.data.remote.home.CategoryResult
import com.umc.umc_6th_wit_android.data.remote.home.HomeService
import com.umc.umc_6th_wit_android.data.remote.home.ProductVer2
import com.umc.umc_6th_wit_android.data.remote.search.Souvenir
import com.umc.umc_6th_wit_android.databinding.ActivityDailyBinding
import com.umc.umc_6th_wit_android.home.CategoryView
import com.umc.umc_6th_wit_android.home.CustomRVAdapter
import com.umc.umc_6th_wit_android.home.ProductDetailFragment
import com.umc.umc_6th_wit_android.home.ranking.CategoryRVAdapter
import com.umc.umc_6th_wit_android.home.ranking.RankingCategoryRVAdapter
import com.umc.umc_6th_wit_android.product.ProductDetailActivity

class DailyActivity : AppCompatActivity(), CategoryView {
    lateinit var binding: ActivityDailyBinding
    private lateinit var adapter: CategoryRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyBinding.inflate(layoutInflater)

        binding.cancelBtn.setOnClickListener {
            finish()
        }
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        // MainActivity로 돌아갈 때 HomeFragment로 전환하도록 플래그 설정
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        intent.putExtra("navigateToHome", true)  // HomeFragment로 이동하기 위한 플래그
        startActivity(intent)
        finish()  // 현재 Activity를 종료
    }
    override fun onResume() {
        super.onResume()
        initCategoryRV()
        getCategory(4,0)//cursor ,페이징은 잠시만
    }
    private fun getCategory(category: Int?, cursor: Int?) {
        val homeService = HomeService(this@DailyActivity)
        homeService.setCategoryView(this)

        homeService.getCategory(category, cursor)
        Log.d("CATEGORYINDEX2",category.toString())
    }

    override fun onGetCategorySuccess(code: String, result: CategoryResult) {
        Log.d("CATEGORY-SUCCESSfragment", code + result)
        if (result.popularProducts !== null && result.nextCursor != null) {
            adapter.addItems(ArrayList(result.popularProducts["생활용품"]))
            adapter.currentCursor = result.nextCursor
        }
        if (result.popularProducts == null && result.nextCursor == null) {
            adapter.currentCursor = 0
        }
    }

    override fun onGetCategoryFailure(code: String, message: String) {
        Log.d("CATEGORY-FAILUREfragment", code + message)
    }
    private fun initCategoryRV(){
        var items: ArrayList<ProductVer2> = ArrayList()

        adapter = CategoryRVAdapter(items , "생활용품", 4){ category, cursor ->
            getCategory(category, cursor)
        }
        adapter.setOnItemClickListener(object : CategoryRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@DailyActivity, ProductDetailActivity::class.java)
                startActivity(intent)
            }
        })
        binding.customRv.adapter = adapter
        binding.customRv.layoutManager  = GridLayoutManager(this@DailyActivity, 2)
    }
}