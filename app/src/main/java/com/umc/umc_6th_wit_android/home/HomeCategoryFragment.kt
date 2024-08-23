package com.umc.umc_6th_wit_android.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.umc_6th_wit_android.data.local.CategoryDao
import com.umc.umc_6th_wit_android.data.remote.home.HomeService
import com.umc.umc_6th_wit_android.data.remote.home.ProductVer2
import com.umc.umc_6th_wit_android.databinding.FragmentHomeCategoryBinding
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.wish.HeartView
import com.umc.umc_6th_wit_android.wish.WishItemResult
import com.umc.umc_6th_wit_android.wish.WishService


class HomeCategoryFragment : Fragment(), HeartView {
    lateinit var binding: FragmentHomeCategoryBinding

    private var categoryKey: String? = null
    private var productList: List<ProductVer2>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // arguments에서 데이터를 가져옵니다.
        arguments?.let {
            categoryKey = it.getString("categoryKey")
            productList = it.getParcelableArrayList("productList")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeCategoryBinding.inflate(inflater, container, false)

//        val items = CategoryDao().items
        val items = ArrayList(productList)

        val adapter = CategoryRVAdapter(
            items,
            { id -> addCart(id) },  // addCart 함수 람다식으로 전달
            { id -> delCart(id) }   // delCart 함수 람다식으로 전달
        )
        adapter.setOnItemClickListener(object : CategoryRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(activity, ProductDetailFragment::class.java)
                startActivity(intent)
//                changeActivity(items[position])
            }
        })

        binding.categoryRv.adapter = adapter
        binding.categoryRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)

        return binding.root
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