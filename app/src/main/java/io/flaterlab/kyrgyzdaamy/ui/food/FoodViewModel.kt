package io.flaterlab.kyrgyzdaamy.ui.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.kyrgyzdaamy.service.ApiResult
import io.flaterlab.kyrgyzdaamy.service.AppPreferences
import io.flaterlab.kyrgyzdaamy.service.NetworkRepositoryMod
import io.flaterlab.kyrgyzdaamy.service.model.BaseResponse
import io.flaterlab.kyrgyzdaamy.service.model.RestaurantResponse
import io.flaterlab.kyrgyzdaamy.service.response.CategoriesResponse
import io.flaterlab.kyrgyzdaamy.service.response.CreateOrder
import io.flaterlab.kyrgyzdaamy.service.response.RobokassaResponse
import io.flaterlab.kyrgyzdaamy.service.response.ScheduleResponse
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

    fun getSchedules(): LiveData<ApiResult<BaseResponse<ScheduleResponse>>> {
        return network.getSchedules()
    }


    fun getRestaurantData(): LiveData<ApiResult<BaseResponse<RestaurantResponse>>> {
        return network.restaurantById(AppPreferences.idOfRestaurant())
    }

    fun categoriesByRestaurantId(): LiveData<ApiResult<BaseResponse<CategoriesResponse>>> {
        return network.getMenuCategories(AppPreferences.idOfRestaurant())
    }
}