package com.umc.umc_6th_wit_android.product

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.umc.umc_6th_wit_android.PriceActivity
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.data.remote.product.ProductResult
import com.umc.umc_6th_wit_android.data.remote.product.ProductService
import com.umc.umc_6th_wit_android.databinding.ActivityProductDetailBinding
import com.umc.umc_6th_wit_android.home.ProductDetailFragment
import com.umc.umc_6th_wit_android.login.TokenManager
import com.umc.umc_6th_wit_android.wish.CartItem
import com.umc.umc_6th_wit_android.wish.FolderActivity
import com.umc.umc_6th_wit_android.wish.FolderPopUpAdapter
import com.umc.umc_6th_wit_android.wish.SelectionListener
import com.umc.umc_6th_wit_android.wish.WishBoardItemResult
import com.umc.umc_6th_wit_android.wish.WishBoardListDelRequest
import com.umc.umc_6th_wit_android.wish.WishItem
import com.umc.umc_6th_wit_android.wish.WishListAddRequest
import com.umc.umc_6th_wit_android.wish.WishService
import java.io.Serializable
import java.text.NumberFormat
import java.util.Locale
import kotlin.properties.Delegates

class ProductDetailActivity : AppCompatActivity(), SelectionListener, ProductView {

    lateinit var binding: ActivityProductDetailBinding
    private var isHelpIv by Delegates.notNull<Boolean>()
    private lateinit var folderPopUpAdapter: FolderPopUpAdapter
     private val ADD_FOLDER_REQUEST_CODE = 1
     private var product_id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

 /*       //Activity 내에 Fragment 적용
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_product_detail, ProductDetailFragment())
                .commit()
        }*/
        product_id = intent.getIntExtra("id", -1)

        // UI 초기화 부분
        binding.comparisonBtnIv.setOnClickListener {
            val intent = Intent(this, PriceActivity::class.java)
            startActivity(intent)
        }

        binding.backBtnIv.setOnClickListener {
            finish() //액티비티라 종료로 가능
        }

        binding.heartBtnIv.setOnClickListener {
            // 하트 버튼 이미지 변경 로직
            if (isHelpIv) {
                val productService = ProductService(this@ProductDetailActivity)
                productService.setProductDetailView(this)
                productService.addCart(intent.getIntExtra("id", -1))   //product_id 넣기
                binding.heartBtnIv.setImageResource(R.drawable.heart_btn_full_image)
                binding.heartBtnTv.text = "${binding.heartBtnTv.text.toString().toInt().plus(1)}"
            } else {
                val request = WishBoardListDelRequest(
                    product_ids = listOf(product_id)   //product_id 넣기
                )
                val productService = ProductService(this@ProductDetailActivity)
                productService.setProductDetailView(this)
                productService.delCart(request)
                binding.heartBtnIv.setImageResource(R.drawable.heart_btn_empty_image)
                binding.heartBtnTv.text = "${binding.heartBtnTv.text.toString().toInt().minus(1)}"
            }
            isHelpIv = !isHelpIv // 하트 버튼 상태 변경

            // DB 하트 숫자 변경 (여기에 DB 처리 로직 추가)
        }

        binding.keepBtnIv.setOnClickListener{
            addToFolder()
        }
    }

    override fun onGetProductSuccess(code: String, result: ProductResult) {
        Log.d("Product-SUCCESS", code + result.name)

        //정보 가져 오는데 성공 -> 뷰에 반영
        val imageUri = Uri.parse(result.image)
        binding.productDetailImg.let { imageView ->
            result.image?.let { imageUrl ->
                Glide.with(imageView.context)
                    .load(imageUrl)
                    .into(imageView)
            }
        }

        binding.productNameTv.text = "${result.name}"
        //binding.heartBtnTv.text = "${result.}" 제품 장바구니에 담기, 빼기 하트 숫자에 적용해야함
        calculatePrice(result.en_price.toDouble(), result.won_price.toDouble())
        binding.whereWidget1Tv.text = "${result.sales_area}"
        binding.whereWidget2Tv.text = "${result.sales_area}"
        if(result.is_heart == 1){
            isHelpIv = false
            binding.heartBtnIv.setImageResource(R.drawable.heart_btn_full_image)
        } else {
            isHelpIv = true
            binding.heartBtnIv.setImageResource(R.drawable.heart_btn_empty_image)
        }
        binding.heartBtnTv.text = "${result.heart_count}"

        //ProductDetailFragment에 정보 전달
        val id = intent.getIntExtra("id", -1)
        val fragment = ProductDetailFragment.newInstance("$id", "${result.name}", "${result.product_type}", result.review_count,"${result.manufacturing_country}" )
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_product_detail, fragment)
            .commit()
    }

     fun loadMoreBoards(cursor: Int?, limit: Int?) {
         val productService = ProductService(this@ProductDetailActivity)
         productService.setProductDetailView(this)
         productService.getWishBoardList(cursor, limit)
     }

     // 폴더에 추가하는 기능을 구현하는 함수
     private fun addToFolder() {

         val wishService = WishService()
         // 폴더 담기 팝업 레이아웃을 인플레이트하고 다이얼로그를 생성
         val dialogView = layoutInflater.inflate(R.layout.folder_select_popup, null)
         val dialog = Dialog(this)
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
         dialog.setContentView(dialogView)

         //다이얼로그 어댑터 연결
         folderPopUpAdapter = FolderPopUpAdapter(mutableListOf(), this) { currentCursor, limit ->
             loadMoreBoards(currentCursor, limit)
         }
         val recyclerView = dialogView.findViewById<RecyclerView>(R.id.folder_recyclerView)
         recyclerView.layoutManager = LinearLayoutManager(this) // LayoutManager 설정
         recyclerView.adapter = folderPopUpAdapter
         // 처음 데이터 로드
         folderPopUpAdapter.resetBoards()
         loadMoreBoards(1, 20)

         // 팝업 창을 중앙에 배치하고 가로 넓이를 300dp로 설정
         val metrics = resources.displayMetrics
         val width = (300 * metrics.density).toInt()

         dialog.window?.setLayout(
             width,
             WindowManager.LayoutParams.WRAP_CONTENT
         )
         dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
         dialog.window?.attributes?.gravity = Gravity.CENTER

         // 새 폴더 버튼 클릭 이벤트 리스너 설정
         dialogView.findViewById<Button>(R.id.btn_new_folder).setOnClickListener {
             val intent = Intent(this, FolderActivity::class.java)

             val selectedItemsList = arrayListOf(
                 WishItem(product_id, binding.productNameTv.text.toString(), 0, 0, R.drawable.item_ex.toString(), 4.4, 1, true),
             )
             // getSelectedItems()의 리턴값을 ArrayList로 변환하여 추가
             intent.putExtra("selected_items", selectedItemsList as Serializable)

             startActivityForResult(intent, ADD_FOLDER_REQUEST_CODE)
             dialog.dismiss()
         }
         // 담기 완료 버튼 클릭 이벤트 리스너 설정
         dialogView.findViewById<Button>(R.id.btn_add_folder).setOnClickListener {
             val request = WishListAddRequest(
                 product_ids = arrayListOf(intent.getIntExtra("id", -1)),
                 folder_id = folderPopUpAdapter.selectedFolders.map { it.folder_id }
             )
             val productService = ProductService(this@ProductDetailActivity)
             productService.setProductDetailView(this)
             productService.postWishtoBoard(request)

             dialog.dismiss()
         }
         dialog.show()
     }

    override fun onGetProductFailure(code: String, message: String) {
        Log.d("Product-FAILURE", code)
    }

     override fun onGetWishBoardListSuccess(code: String, result: WishBoardItemResult) {
         Log.d("board_popup", "Success")
         folderPopUpAdapter.addBoards(result.folders)
         folderPopUpAdapter.currentCursor = result.nextCursor
     }

     override fun onGetWishBoardListFailure(code: String, message: String) {
         Log.d("board", "Fail")
     }

     override fun onPostWishtoBoardSuccess(code: String, result: WishBoardItemResult) {
         TODO("Not yet implemented")
     }

     override fun onPostWishtoBoardFailure(code: String, message: String) {
         TODO("Not yet implemented")
     }

     override fun onPostAddCartSuccess(code: String, response: CartItem) {
        TODO("Not yet implemented")
    }

    override fun onPostAddCartFailure(code: String, error: String) {
        TODO("Not yet implemented")
    }

    override fun onPostDelCartSuccess(code: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun onPostDelCartFailure(code: String, message: String) {
        TODO("Not yet implemented")
    }

    override fun onResume() {
        super.onResume()
        getProductDetail()
    }

    private fun getProductDetail() {
        val id = intent.getIntExtra("id", -1)  // 기본값을 -1로 설정
        Log.d("PRODUCT_DETAIL", id.toString())
        if (id != -1) {
            Log.d("ProductDetailActivity", "Ok id received")
            val productService = ProductService(this@ProductDetailActivity)
            productService.setProductDetailView(this)
            productService.getProductDetail(id)

        }else{
            Log.d("ProductDetailActivity", "No id received") }
    }

    private fun calculatePrice (enPrice: Double, wonPrice: Double) {
        // 일본 엔화 포맷
        val yenFormat = NumberFormat.getCurrencyInstance(Locale.JAPAN)
        val formattedYenPrice = yenFormat.format(enPrice)
        binding.currencyYenTv.text = "${formattedYenPrice}"

        // 한국 원화 포맷
        val wonFormat = NumberFormat.getCurrencyInstance(Locale.KOREA)
        val formattedWonPrice = wonFormat.format(wonPrice)
        binding.currencyWonTv.text = "${formattedWonPrice}"
    }

     override fun onSelectionItemChanged(count: Int) {
         updateItemButtonState(count)
     }

     override fun onSelectionBoardChanged(count: Int) {
         updateBoardButtonState(count)
     }

     private fun updateItemButtonState(selectedItemCount: Int) {
         val isEnabled = selectedItemCount > 0
     }

     // 보드 선택 상태에 따라 버튼의 활성화를 업데이트하는 함수
     private fun updateBoardButtonState(selectedBoardCount: Int) {
         val isEnabled = selectedBoardCount > 0
         val isEnabledOne = selectedBoardCount == 1
     }

 }
