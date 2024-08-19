package com.umc.umc_6th_wit_android.data.local

import com.umc.umc_6th_wit_android.R

class ReviewDetailDao {
    val items = ArrayList<ReviewDetailDto>()
    init{
        items.add(ReviewDetailDto(R.drawable.user1_image, 1, "쩝쩝박사", 2022, 4.0,
            "미니 사이즈와 큰 사이즈가 있어서 더욱 좋았어요~\n맛은 고소하고 아이들과 간식으로 먹기 좋아요!\n돈키호테에서 쉽게 구할 수 있으니 꼭 구매하세요!", false, 22))

        items.add(ReviewDetailDto(R.drawable.user2_image, 2, "천재왕", 2023, 4.0,
            "미니 사이즈와 큰 사이즈가 있어서 더욱 좋았어요~\n맛은 고소하고 아이들과 간식으로 먹기 좋아요!\n돈키호테에서 쉽게 구할 수 있으니 꼭 구매하세요!", false, 22))

        items.add(ReviewDetailDto(R.drawable.user3_image, 3, "여행좋아", 2024, 4.5,
        "미니 사이즈와 큰 사이즈가 있어서 더욱 좋았어요~\n맛은 고소하고 아이들과 간식으로 먹기 좋아요!\n돈키호테에서 쉽게 구할 수 있으니 꼭 구매하세요!", false, 22))
    }
}