package com.umc.umc_6th_wit_android.home.notice

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.umc.umc_6th_wit_android.data.local.NoticeDto
import com.umc.umc_6th_wit_android.data.remote.notice.Notice
import com.umc.umc_6th_wit_android.data.remote.notice.NoticeService
import com.umc.umc_6th_wit_android.databinding.ActivityNoticeBinding

class NoticeActivity : AppCompatActivity(), NoticeView{
    lateinit var binding: ActivityNoticeBinding
    private lateinit var adapter: NoticeRVAdapter
    private val items = ArrayList<NoticeDto>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)

        binding.backBtn.setOnClickListener {
            finish()
        }
//        items.add(NoticeDto("[서비스점검공지] 9/25(화)12:00~13:00","2024-08-23"))
//        items.add(NoticeDto("[서비스점검공지] 9/25(화)12:00~13:00","2024-08-23"))
//        items.add(NoticeDto("[서비스점검공지] 9/25(화)12:00~13:00","2024-08-23"))

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        getNotices()
    }

    private fun getNotices() {
        val noticeService = NoticeService()
        noticeService.setNoticeView(this)

        noticeService.getNotices()
    }
    private fun initRecyclerView(result: List<Notice>) {
        adapter = NoticeRVAdapter(result)

        binding.noticeRv.adapter = adapter
        binding.noticeRv.layoutManager = LinearLayoutManager(this@NoticeActivity, LinearLayoutManager.VERTICAL,false)

    }

    override fun onGetNoticeSuccess(code: String, result: List<Notice>) {
        Log.d("NOTICE-SUCCESS/NOTICE-RESPONSE", code)
        initRecyclerView(result)
    }

    override fun onGetNoticeFailure(code: String, message: String) {
        Log.d("NOTICE-FAILURE/NOTICE-RESPONSE", message)
    }
}