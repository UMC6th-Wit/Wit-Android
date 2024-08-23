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
import com.umc.umc_6th_wit_android.databinding.ActivityFoodBinding
import com.umc.umc_6th_wit_android.home.CategoryView
import com.umc.umc_6th_wit_android.home.ranking.CategoryRVAdapter
import com.umc.umc_6th_wit_android.product.ProductDetailActivity
import com.umc.umc_6th_wit_android.wish.HeartView

class FoodActivity : AppCompatActivity(), CategoryView , HeartView {
    lateinit var binding: ActivityFoodBinding
    private lateinit var adapter: CategoryRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater)

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
        getCategory(1,0)//cursor ,페이징은 잠시만
    }
    private fun getCategory(category: Int?, cursor: Int?) {
        val homeService = HomeService(this@FoodActivity)
        homeService.setCategoryView(this)

        homeService.getCategory(category, cursor)
        Log.d("CATEGORYINDEX2",category.toString())
    }

    override fun onGetCategorySuccess(code: String, result: CategoryResult) {
        Log.d("CATEGORY-SUCCESS", code + result)
        if (result.popularProducts !== null && result.nextCursor != null) {
            adapter.addItems(ArrayList(result.popularProducts["식품"]))
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

        adapter = CategoryRVAdapter(
            items ,
            "식품",
            1,
            { category, cursor -> getCategory(category, cursor)},
            { id -> addCart(id) },  // addCart 함수 람다식으로 전달
            { id -> delCart(id) } )
        adapter.setOnItemClickListener(object : CategoryRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@FoodActivity, ProductDetailActivity::class.java)
                intent.putExtra("id", items[position].id)
                startActivity(intent)
            }
        })
        binding.customRv.adapter = adapter
        binding.customRv.layoutManager  = GridLayoutManager(this@FoodActivity, 2)
    }
    private fun addCart(id:Int) {
        val homeService = HomeService(this)
        homeService.setHeartView(this)
        homeService.addCart(id)
    }
    private fun delCart(id:Int) {
        val homeService = HomeService(this)
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