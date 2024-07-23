package com.umc.umc_6th_wit_android.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.umc.umc_6th_wit_android.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.kakaoLoginButton.setOnClickListener {
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
        }
        binding.naverLoginButton.setOnClickListener {
            val intent = Intent(this, OnboardingActivity::class.java)
            startActivity(intent)
        }



    }
}