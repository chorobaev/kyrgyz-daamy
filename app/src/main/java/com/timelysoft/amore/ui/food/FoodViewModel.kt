package com.timelysoft.amore.ui.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timelysoft.amore.adapter.food.BasketCommands
import com.timelysoft.amore.bottomsheet.basket.Mode
import com.timelysoft.amore.service.ApiResult
import com.timelysoft.amore.service.AppPreferences
import com.timelysoft.amore.service.NetworkRepositoryMod

import com.timelysoft.amore.service.model.BaseResponse
import com.timelysoft.amore.service.model.RestaurantResponse
import com.timelysoft.amore.service.response.*


class FoodViewModel(private val network: NetworkRepositoryMod) :ViewModel(){

    private val _categoryIdLiveData: MutableLiveData<String> = MutableLiveData()
    private val _categoryName: MutableLiveData<String> = MutableLiveData()
    val _categoryItemResponse: MutableLiveData<CategoryItemResponse> = MutableLiveData()


    val categoryLiveData: LiveData<String>
        get() = _categoryIdLiveData

    val categoryNameLiveData: LiveData<String>
        get() = _categoryName


    fun setCategoryId(categoryId: String, categoryName: String) {
        _categoryIdLiveData.value = categoryId
        _categoryName.value = categoryName
    }

    fun insertMenuItem(menuItem: MenuItem, index: Int, modGroup: List<BaseModifierGroup>, mode: Mode) {
        BasketCommands.insertMenuItemSecondVersion(menuItem, index, modGroup, mode)
    }

    fun deleteMenuItem(position: Int) {
        BasketCommands.deleteFromBasket(position)
    }

    fun getBasketElements(): LiveData<List<MenuItem>> {
        return BasketCommands.liveDataOfMenuItems
    }

    fun orderValidate(orderModel: ValidateOrder): LiveData<ApiResult<OrderValidateResponse?>> {
        return network.orderValidate(orderModel)

    }

    fun orderCreate(orderModel: CreateOrder): LiveData<ApiResult<String?>> {
        return network.orderCreate(orderModel)
    }

    fun payOnline(
        restaurantId: String,
        orderId: String
    ): LiveData<ApiResult<RobokassaResponse?>> {
        return network.onlinePayment(restaurantId, orderId)
    }

    fun getSchedules() :LiveData<ApiResult<ScheduleResponse>> = network.getSchedules()

    fun restaurants(): LiveData<ApiResult<List<RestaurantResponse>>> {
        return network.restaurants()
    }

    fun cities(): LiveData<ApiResult<List<CityRestResponse>?>> {
        return network.cities()
    }

    fun streets(id: Int): LiveData<ApiResult<List<StreetResponse>?>> {
        return network.streets(id)
    }

    fun categoriesByRestaurantId(id: String):LiveData<ApiResult<BaseResponse<CategoriesResponse>?>> {
        return network.getMenuCategories(id)
    }

    fun itemsByCategories(
        categoryId: String
    ): LiveData<ApiResult<CategoryItemResponse>> {
        return network.foodItemByCategoryId(categoryId)
    }


    fun warning(): LiveData<ApiResult<String?>> {
        return network.warning()
    }


}