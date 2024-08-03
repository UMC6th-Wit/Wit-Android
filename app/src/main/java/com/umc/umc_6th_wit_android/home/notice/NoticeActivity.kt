package com.umc.umc_6th_wit_android.home.notice

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.umc_6th_wit_android.data.local.NoticeDto
import com.umc.umc_6th_wit_android.databinding.ActivityNoticeBinding

class NoticeActivity : AppCompatActivity() {
    lateinit var binding: ActivityNoticeBinding

    private val items = ArrayList<NoticeDto>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)

        binding.backBtn.setOnClickListener {
            finish()
        }
        items.add(NoticeDto("[서비스점검공지] 9/25(화)12:00~13:00","2024-08-23"))
        items.add(NoticeDto("[서비스점검공지] 9/25(화)12:00~13:00","2024-08-23"))
        items.add(NoticeDto("[서비스점검공지] 9/25(화)12:00~13:00","2024-08-23"))

        // 리사이클러뷰 설정
        val adapter = NoticeRVAdapter(items)
        binding.noticeRv.adapter = adapter
        binding.noticeRv.layoutManager = LinearLayoutManager(this@NoticeActivity, LinearLayoutManager.VERTICAL,false)

        setContentView(binding.root)
    }
}