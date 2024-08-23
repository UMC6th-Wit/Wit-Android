package com.umc.umc_6th_wit_android.home.ranking

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.umc_6th_wit_android.data.remote.home.CategoryResult
import com.umc.umc_6th_wit_android.data.remote.home.HomeService
import com.umc.umc_6th_wit_android.data.remote.home.ProductVer2
import com.umc.umc_6th_wit_android.databinding.FragmentRankingCategoryBinding
import com.umc.umc_6th_wit_android.home.CategoryView
import com.umc.umc_6th_wit_android.product.ProductDetailActivity
import com.umc.umc_6th_wit_android.wish.HeartView


class RankingCategoryFragment(private val category : Int) : Fragment() , CategoryView , HeartView{
    lateinit var binding: FragmentRankingCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankingCategoryBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onResume() {
        super.onResume()
        getCategory(category,0)
        Log.d("CATEGORYINDEX",category.toString())
    }
    private fun getCategory(category: Int?, cursor: Int?) {
        val homeService = HomeService(requireContext())
        homeService.setCategoryView(this)

        homeService.getCategory(category, cursor)
        Log.d("CATEGORYINDEX2",category.toString())
    }

    override fun onGetCategorySuccess(code: String, result: CategoryResult) {
        Log.d("CATEGORY-SUCCESSfragment", code + result)
        initCategoryRV(result.popularProducts!!)
    }

    override fun onGetCategoryFailure(code: String, message: String) {
        Log.d("CATEGORY-FAILUREfragment", code + message)
    }
    private fun initCategoryRV(result: Map<String, List<ProductVer2>>){
        // 카테고리 키 리스트
        val categories = listOf("ALL", "식품", "뷰티코스메틱", "의약품", "생활용품")
        // 각 카테고리에 해당하는 키
        val categoryKey = categories[category]

/*        val products = ArrayList<ProductVer2>()

        products.add(ProductVer2(
            146,
            "Ｐ＆Ｇ アリエール ジェルボール4D 洗",
            4639,
            498,
            "https://donki-ec-static-1306051524.file.myqcloud.com/images/4987176062703_0516_1.jpg",
            "",
            0,
            false,
            0,
            0f
        ))

        products.add(ProductVer2(
            177,
            "Ｐ＆Ｇジャパン ファブリーズＷ消臭 トイ",
            5124,
            550,
            "https://donki-ec-static-1306051524.file.myqcloud.com/images/4987176165206.jpg",
            "",
            0,
            false,
            0,
            0f
        ))

        for (i in 0 until 10) {
            products.add(products[1])
            products.add(products[2])
        }*/


        val items = ArrayList(result[categoryKey])

//        val items = ArrayList(products)
        val adapter = RankingCategoryRVAdapter(
            items,
            "ranking",
            { id -> addCart(id) },
            { id -> delCart(id) } )
        adapter.setOnItemClickListener(object : RankingCategoryRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(activity, ProductDetailActivity::class.java)
                startActivity(intent)
//                changeActivity(items[position])
            }
        })
        binding.rankingCategoryRv.adapter = adapter
        binding.rankingCategoryRv.layoutManager  = GridLayoutManager(context, 2)
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