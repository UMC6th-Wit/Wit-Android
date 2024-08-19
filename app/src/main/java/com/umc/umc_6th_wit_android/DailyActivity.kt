package com.umc.umc_6th_wit_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.umc.umc_6th_wit_android.data.local.CategoryDao
import com.umc.umc_6th_wit_android.data.local.CategoryDto
import com.umc.umc_6th_wit_android.databinding.ActivityBestFoodBinding
import com.umc.umc_6th_wit_android.databinding.ActivityDailyBinding
import com.umc.umc_6th_wit_android.home.CustomRVAdapter
import com.umc.umc_6th_wit_android.home.ProductDetailActivity

class DailyActivity : AppCompatActivity() {
    lateinit var binding: ActivityDailyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyBinding.inflate(layoutInflater)
        val items = CategoryDao().items

        binding.cancelBtn.setOnClickListener {
            finish()
        }
//test data
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))

        val adapter = CustomRVAdapter(items)
        adapter.setOnItemClickListener(object : CustomRVAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                val intent = Intent(this@DailyActivity, ProductDetailActivity::class.java)
                startActivity(intent)
//                changeActivity(items[position])
            }
        })
        binding.customRv.adapter = adapter
        binding.customRv.layoutManager  = GridLayoutManager(this@DailyActivity, 2)

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
}