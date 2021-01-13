package com.timelysoft.kainarapp.ui.restaurant

import android.app.Application
import androidx.lifecycle.LiveData
import com.timelysoft.kainarapp.base.BaseViewModel
import com.timelysoft.kainarapp.service.ApiResult
import com.timelysoft.kainarapp.service.Resource
import com.timelysoft.kainarapp.service.model2.BaseResponse
import com.timelysoft.kainarapp.service.model2.Restaurant
import com.timelysoft.kainarapp.service.model2.RestaurantGroupResponse
import com.timelysoft.kainarapp.service.model2.RestaurantResponse

class RestaurantViewModel(application: Application) : BaseViewModel(application) {


    fun restaurants(): LiveData<ApiResult<List<RestaurantResponse>>> {
        return network.restaurants()
    }

    fun restaurant(restaurantId: String): LiveData<ApiResult<BaseResponse<RestaurantResponse>>> {
        return network.restaurantById(restaurantId)
    }
}