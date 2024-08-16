package com.umc.umc_6th_wit_android.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

    }
}