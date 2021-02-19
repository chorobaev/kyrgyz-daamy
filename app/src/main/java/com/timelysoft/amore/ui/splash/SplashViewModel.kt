package com.timelysoft.amore.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.timelysoft.amore.service.ApiResult
import com.timelysoft.amore.service.NetworkRepositoryMod
import com.timelysoft.amore.service.model.RestaurantResponse

class SplashViewModel(private val networkRepositoryMod: NetworkRepositoryMod)  : ViewModel(){
    fun getRestaurant(): LiveData<ApiResult<List<RestaurantResponse>>>{
        return networkRepositoryMod.restaurants()
    }
}