package com.umc.umc_6th_wit_android.wish

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.umc_6th_wit_android.databinding.ActivityWishBinding

class WishActivity : AppCompatActivity() {
    lateinit var binding: ActivityWishBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWishBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}