package com.umc.umc_6th_wit_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.umc_6th_wit_android.databinding.ActivityPriceBinding

class PriceActivity : AppCompatActivity() {
    lateinit var binding: ActivityPriceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPriceBinding.inflate(layoutInflater)

        binding.backBtn.setOnClickListener {
            finish()
        }
        setContentView(binding.root)
    }
}