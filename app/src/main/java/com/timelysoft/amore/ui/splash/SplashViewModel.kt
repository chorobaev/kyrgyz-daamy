package com.timelysoft.amore.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.timelysoft.amore.service.ApiResult
import com.timelysoft.amore.service.NetworkRepositoryMod
import com.timelysoft.amore.service.model2.AuthBody
import com.timelysoft.amore.service.model2.response2.AccessToken


class SplashViewModel(private val networkRepositoryMod: NetworkRepositoryMod)  : ViewModel(){

    fun sendAuthCredentials(authBody: AuthBody): LiveData<ApiResult<AccessToken?>>{
        return networkRepositoryMod.getToken(authBody)
    }
}