package com.umc.umc_6th_wit_android.data.local

import com.umc.umc_6th_wit_android.R
import ddwu.com.mobile.finalreport.data.PersonalDto

//임시 test data
class PersonalDao {
    val items = ArrayList<PersonalDto>()

    init{
        items.add(PersonalDto(R.drawable.rv1, "긴자이치고 딸기(딸기빵)", "367", "0", "4.40", 273))
        items.add(PersonalDto(R.drawable.rv2, "로이히츠보코 156개", "551", "0", "4.0", 145))
        items.add(PersonalDto(R.drawable.rv3, "휴족미려 24개", "759", "0", "3.8", 78))
        items.add(PersonalDto(R.drawable.rv2, "로이히츠보코 156개", "551", "0", "4.0", 145))
        items.add(PersonalDto(R.drawable.rv1, "긴자이치고 딸기(딸기빵)", "367", "0", "4.40", 273))
    }
}