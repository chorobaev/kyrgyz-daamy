package com.timelysoft.amore.ui.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timelysoft.amore.BasketCommands
import com.timelysoft.amore.service.ApiResult
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.NetworkRepositoryMod

import com.timelysoft.amore.service.model.BaseResponse
import com.timelysoft.amore.service.model.RestaurantResponse
import com.timelysoft.amore.service.response.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.registerSerializer
import javax.inject.Inject


@HiltViewModel
class FoodViewModel @Inject constructor(private val network: NetworkRepositoryMod) : ViewModel() {



    fun orderCreate(orderModel: CreateOrder): LiveData<ApiResult<BaseResponse<String>>> {
        return network.orderCreate(orderModel)
    }

    fun payOnline(
        restaurantId: String,
        orderId: String
    ): LiveData<ApiResult<BaseResponse<RobokassaResponse>>> {
        return network.onlinePayment(restaurantId, orderId)
    }

    fun getSchedules() :LiveData<ApiResult<BaseResponse<ScheduleResponse>>>{
        return network.getSchedules()
    }


    fun getRestaurantData() :LiveData<ApiResult<BaseResponse<RestaurantResponse>>>{
        return network.restaurantById(AppPreferences.idOfRestaurant())
    }

    fun categoriesByRestaurantId():LiveData<ApiResult<BaseResponse<CategoriesResponse>>> {
        return network.getMenuCategories(AppPreferences.idOfRestaurant())
    }
}