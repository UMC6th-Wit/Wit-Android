package com.umc.umc_6th_wit_android.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.umc_6th_wit_android.data.remote.home.HomePersonalResult
import com.umc.umc_6th_wit_android.data.remote.home.HomeService
import com.umc.umc_6th_wit_android.data.remote.home.Product
import com.umc.umc_6th_wit_android.databinding.ActivityCustomBinding
import com.umc.umc_6th_wit_android.product.ProductDetailActivity
import java.util.ArrayList

class CustomActivity : AppCompatActivity() , PersonalView {
    lateinit var binding: ActivityCustomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomBinding.inflate(layoutInflater)

        binding.cancelBtn.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        getRecommendProducts()
    }

    private fun getRecommendProducts() {
        val homeService = HomeService(this@CustomActivity)
        homeService.setPersonalView(this)

        homeService.getRecommendProducts()
    }

    override fun onGetPersonalSuccess(code: String, result: HomePersonalResult) {
        Log.d("PERSONAL-SUCCESS", code + result.user.username + result.recommendations)
        binding.title.text = "${result.user.username}님을 위한 추천템"
        initCustomRV(result.recommendations)
    }

    override fun onGetPersonalFailure(code: String, message: String) {
        Log.d("PERSONAL-FAILURE", code)
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
                val intent = Intent(this@CustomActivity, ProductDetailActivity::class.java)
                startActivity(intent)
//                changeActivity(items[position])
            }
        })
        binding.customRv.layoutManager = GridLayoutManager(this@CustomActivity, 2)
        binding.customRv.adapter = adapter
    }
}