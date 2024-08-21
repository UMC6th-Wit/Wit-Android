package com.umc.umc_6th_wit_android.home.notice

import com.umc.umc_6th_wit_android.data.remote.notice.Notice

interface NoticeView {
    fun onGetNoticeSuccess(code: String, result: List<Notice>)
    fun onGetNoticeFailure(code: String, message: String)
}