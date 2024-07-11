package com.umc.umc_6th_wit_android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umc.umc_6th_wit_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}