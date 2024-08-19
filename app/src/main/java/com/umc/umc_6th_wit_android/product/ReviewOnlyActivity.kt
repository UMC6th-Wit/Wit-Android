package com.umc.umc_6th_wit_android.product

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.umc_6th_wit_android.databinding.ActivityReviewOnlyBinding

class ReviewOnlyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReviewOnlyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewOnlyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goToReviewWriteBtnTv.setOnClickListener {
            val intent = Intent(this, ReviewWriteActivity::class.java)
            startActivity(intent)
        }


    }
}