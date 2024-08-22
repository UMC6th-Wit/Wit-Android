package com.umc.umc_6th_wit_android.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> get() = _name

    private val _nickname = MutableLiveData<String>()
    val nickname: LiveData<String> get() = _nickname

    private val _birthdate = MutableLiveData<String>()
    val birthdate: LiveData<String> get() = _birthdate

    private val _gender = MutableLiveData<String>()
    val gender: LiveData<String> get() = _gender


    fun setName(newName: String) {
        _name.value = newName
    }

    fun setNickname(newNickname: String) {
        _nickname.value = newNickname
    }

    fun setBirthdate(newBirthdate: String) {
        _birthdate.value = newBirthdate
    }

    fun setGender(newGender: String) {
        _gender.value = newGender
    }
}
