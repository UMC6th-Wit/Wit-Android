package com.umc.umc_6th_wit_android.data.remote.notice
import android.util.Log
import com.umc.umc_6th_wit_android.home.notice.NoticeView
import com.umc.umc_6th_wit_android.utils.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeService() {
    private lateinit var noticeView: NoticeView

    fun setNoticeView(noticeView: NoticeView) {
        this.noticeView = noticeView
    }

    fun getNotices() {
        val noticeService = getRetrofit().create(NoticeRetrofitInterface::class.java)

        noticeService.getNotices().enqueue(object : Callback<NoticeResponse> {
            override fun onResponse(call: Call<NoticeResponse>, response: Response<NoticeResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val noticeResponse: NoticeResponse = response.body()!!

                    Log.d("NOTICE-RESPONSE", noticeResponse.toString())

                    when (val code = noticeResponse.code) {
                        "POST200" -> {
                            noticeView.onGetNoticeSuccess(code, noticeResponse.result!!)
                        }

                        else -> noticeView.onGetNoticeFailure(code, noticeResponse.message)
                    }
                }
            }

            override fun onFailure(call: Call<NoticeResponse>, t: Throwable) {
                noticeView.onGetNoticeFailure("NOTICE400", "공지사항 목록 조회에 실패했습니다.")
            }
        })
    }
}