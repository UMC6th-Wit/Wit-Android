package com.umc.umc_6th_wit_android.product

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.umc.umc_6th_wit_android.R
import com.umc.umc_6th_wit_android.databinding.ActivityReviewMinBinding

class ReviewMinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewMinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewMinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_product_detail, ProductDetailFragment())
                .commit()
        }

        binding.goToReviewBtnIv.setOnClickListener {
            val intent = Intent(this, ReviewOnlyActivity::class.java)
            startActivity(intent)
        }

        binding.moreReviewBtnIv.setOnClickListener{
            val intent = Intent(this, ReviewOnlyActivity::class.java)
            startActivity(intent)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemList = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
        val adapter = ReviewMinAdapter(itemList)
        recyclerView.adapter = adapter

    }
}