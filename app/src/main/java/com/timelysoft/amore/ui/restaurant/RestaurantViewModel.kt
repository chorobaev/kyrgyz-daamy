package com.timelysoft.amore.ui.restaurant

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.timelysoft.amore.service.ApiResult
import com.timelysoft.amore.service.NetworkRepositoryMod
import com.timelysoft.amore.service.model2.BaseResponse
import com.timelysoft.amore.service.model2.RestaurantResponse

class RestaurantViewModel(private val network : NetworkRepositoryMod) : ViewModel() {


    fun restaurants(): LiveData<ApiResult<List<RestaurantResponse>>> {
        return network.restaurants()
    }

    fun restaurant(restaurantId: String): LiveData<ApiResult<BaseResponse<RestaurantResponse>>> {
        return network.restaurantById(restaurantId)
    }
}