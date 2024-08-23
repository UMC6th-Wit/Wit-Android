package com.umc.umc_6th_wit_android.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.umc_6th_wit_android.data.remote.home.HomeBestFoodResult
import com.umc.umc_6th_wit_android.data.remote.home.HomeService
import com.umc.umc_6th_wit_android.data.remote.home.Product
import com.umc.umc_6th_wit_android.databinding.ActivityBestFoodBinding
import com.umc.umc_6th_wit_android.product.ProductDetailActivity
import java.util.ArrayList

class BestFoodActivity  : AppCompatActivity(), BestFoodView {
    lateinit var binding: ActivityBestFoodBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBestFoodBinding.inflate(layoutInflater)

        binding.cancelBtn.setOnClickListener {
            finish()
        }


        setContentView(binding.root)
    }
    override fun onResume() {
        super.onResume()
        getBestFood()
    }

    private fun getBestFood() {
        val homeService = HomeService(this@BestFoodActivity)
        homeService.setBestFoodView(this)

        homeService.getBestFood()
    }
    override fun onGetBestFoodSuccess(code: String, result: HomeBestFoodResult) {
        Log.d("FOOD-SUCCESS1", code + result.nyamRecommendations)
        initCustomRV(result.nyamRecommendations)
    }

    override fun onGetBestFoodFailure(code: String, message: String) {
        Log.d("FOOD-FAILURE", code)
    }
    private fun initCustomRV(result: List<Product>) {
// ArrayList 초기화
//        var items: ArrayList<Product> = ArrayList()
        var items: ArrayList<Product> = ArrayList(result)

        /* // 임의 test data 추가
         items.add(
             Product(
                 1,
                 "포테이토 칩스 우스시오 아지",
                 367,
                 3151,
                 "https://donki-ec-static-1306051524.file.myqcloud.com/images/4901085122365.jpg",
                 10,
                 "",
                 0,
                 1,
                 4
                 )
         )
 */

        val adapter = ProductRVAdapter(items)
        adapter.setOnItemClickListener(object : ProductRVAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@BestFoodActivity, ProductDetailActivity::class.java)
                startActivity(intent)
//                changeActivity(items[position])
            }
        })
        binding.customRv.layoutManager = GridLayoutManager(this@BestFoodActivity, 2)
        binding.customRv.adapter = adapter
    }
}