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
import com.umc.umc_6th_wit_android.data.remote.home.ProductVer2
import com.umc.umc_6th_wit_android.databinding.ActivityCustomBinding
import com.umc.umc_6th_wit_android.product.ProductDetailActivity
import com.umc.umc_6th_wit_android.wish.HeartView
import java.util.ArrayList

class CustomActivity : AppCompatActivity() , PersonalView, HeartView {
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
        Log.d("PERSONAL-SUCCESS", code + result.user.userNickname + result.recommendations)
        binding.title.text = "${result.user.userNickname}님을 위한 추천템"
        initCustomRV(result.recommendations)
    }

    override fun onGetPersonalFailure(code: String, message: String) {
        Log.d("PERSONAL-FAILURE", code)
    }

    private fun initCustomRV(result: List<ProductVer2>) {
// ArrayList 초기화
//        var items: ArrayList<Product> = ArrayList()
        var items: ArrayList<ProductVer2> = ArrayList(result)

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

        val adapter = ProductRVAdapter(
            items,
            { id -> addCart(id) },
            { id -> delCart(id) } )
        adapter.setOnItemClickListener(object : ProductRVAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@CustomActivity, ProductDetailActivity::class.java)
                intent.putExtra("id", items[position].id)
                startActivity(intent)
//                changeActivity(items[position])
            }
        })
        binding.customRv.layoutManager = GridLayoutManager(this@CustomActivity, 2)
        binding.customRv.adapter = adapter
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
