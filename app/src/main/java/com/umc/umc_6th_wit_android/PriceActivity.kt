package com.umc.umc_6th_wit_android

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.umc_6th_wit_android.data.local.NoticeDto
import com.umc.umc_6th_wit_android.databinding.ActivityNoticeBinding
import com.umc.umc_6th_wit_android.databinding.ActivityPriceBinding
import com.umc.umc_6th_wit_android.home.notice.NoticeRVAdapter

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