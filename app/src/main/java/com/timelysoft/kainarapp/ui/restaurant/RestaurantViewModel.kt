package com.timelysoft.kainarapp.ui.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.timelysoft.kainarapp.service.ApiResult
import com.timelysoft.kainarapp.service.NetworkRepositoryMod
import com.timelysoft.kainarapp.service.model2.BaseResponse
import com.timelysoft.kainarapp.service.model2.RestaurantResponse

class RestaurantViewModel(private val network : NetworkRepositoryMod) : ViewModel() {


    fun restaurants(): LiveData<ApiResult<List<RestaurantResponse>>> {
        return network.restaurants()
    }

    fun restaurant(restaurantId: String): LiveData<ApiResult<BaseResponse<RestaurantResponse>>> {
        return network.restaurantById(restaurantId)
    }
}