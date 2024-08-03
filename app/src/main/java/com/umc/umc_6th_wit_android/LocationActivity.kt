package com.umc.umc_6th_wit_android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.umc_6th_wit_android.databinding.ActivityLocationBinding

class LocationActivity: AppCompatActivity() {
    lateinit var binding: ActivityLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)

        binding.backBtn.setOnClickListener {
            finish()
        }
        setContentView(binding.root)
    }
}