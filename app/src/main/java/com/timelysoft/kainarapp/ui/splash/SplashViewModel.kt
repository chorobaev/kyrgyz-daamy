package com.timelysoft.kainarapp.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.timelysoft.kainarapp.service.ApiResult
import com.timelysoft.kainarapp.service.NetworkRepositoryMod
import com.timelysoft.kainarapp.service.model2.AuthBody
import com.timelysoft.kainarapp.service.model2.BaseResponse
import com.timelysoft.kainarapp.service.model2.response2.AccessToken


class SplashViewModel(private val networkRepositoryMod: NetworkRepositoryMod)  : ViewModel(){

    fun sendAuthCredentials(authBody: AuthBody): LiveData<ApiResult<AccessToken?>>{
        return networkRepositoryMod.getToken(authBody)
    }
}