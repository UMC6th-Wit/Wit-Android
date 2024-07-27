package com.umc.umc_6th_wit_android.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _name = MutableLiveData<String>("김가영")
    val name: LiveData<String> get() = _name

    private val _nickname = MutableLiveData<String>("주황색옷을입은고양이")
    val nickname: LiveData<String> get() = _nickname

    private val _birthdate = MutableLiveData<String>("2000.01.01")
    val birthdate: LiveData<String> get() = _birthdate

    fun setName(newName: String) {
        _name.value = newName
    }

    fun setNickname(newNickname: String) {
        _nickname.value = newNickname
    }
    fun setBirthdate(newBirthdate: String) {
        _birthdate.value = newBirthdate
    }
}
