package com.umc.umc_6th_wit_android.data.remote.notice

import com.google.gson.annotations.SerializedName

data class NoticeResponse(
    @SerializedName (value = "isSuccess") val isSuccess: Boolean,
    @SerializedName (value = "code") val code:String,
    @SerializedName (value = "message") val message:String,
    @SerializedName (value = "result") val result: List<Notice>
)

data class Notice(
    @SerializedName("notice_id") val noticeId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val createdAt : String,
    @SerializedName("updated_at") val updatedAt : String
)