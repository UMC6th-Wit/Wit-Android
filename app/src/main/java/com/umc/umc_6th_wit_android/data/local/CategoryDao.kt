package com.umc.umc_6th_wit_android.data.local

import com.umc.umc_6th_wit_android.R
import ddwu.com.mobile.finalreport.data.PersonalDto

class CategoryDao {
    val items = ArrayList<CategoryDto>()
    init{
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", true))
        items.add(CategoryDto(R.drawable.category1, "도쿄 돈키호테", "포테이토 칩스 우스시오 아지", "367¥", "3151₩", false))
    }
}