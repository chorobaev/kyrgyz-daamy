package com.timelysoft.amore.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel  : ViewModel() {

    private val backToMainLiveData : MutableLiveData<Boolean> = MutableLiveData()
    fun backToMain(backToMain : Boolean) :LiveData<Boolean>{
        backToMainLiveData.value = backToMain
        return backToMainLiveData
    }
}