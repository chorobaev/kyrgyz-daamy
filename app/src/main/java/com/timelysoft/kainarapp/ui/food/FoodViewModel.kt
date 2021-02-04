package com.timelysoft.kainarapp.ui.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timelysoft.kainarapp.adapter.food.BasketCommands
import com.timelysoft.kainarapp.bottomsheet.basket.Mode
import com.timelysoft.kainarapp.service.ApiResult
import com.timelysoft.kainarapp.service.NetworkRepositoryMod

import com.timelysoft.kainarapp.service.model2.BaseResponse
import com.timelysoft.kainarapp.service.model2.RestaurantResponse
import com.timelysoft.kainarapp.service.model2.response2.*


class FoodViewModel(private val network: NetworkRepositoryMod) :ViewModel(){

    private val _categoryIdLiveData: MutableLiveData<String> = MutableLiveData()
    private val _categoryName: MutableLiveData<String> = MutableLiveData()


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

    fun restaurants(): LiveData<ApiResult<List<RestaurantResponse>>> {
        return network.restaurants()
    }

    fun cities(): LiveData<ApiResult<List<CityRestResponse>?>> {
        return network.cities()
    }

    fun streets(id: Int): LiveData<ApiResult<List<StreetResponse>?>> {
        return network.streets(id)
    }

    fun categoriesByRestaurantId(id: String): LiveData<ApiResult<BaseResponse<CategoriesResponse>?>> {
        return network.getMenuCategories(id)
    }

    fun itemsByCategories(
        restaurantId: String,
        categoryId: String
    ): LiveData<ApiResult<CategoryItemResponse>> {
        return network.foodItemByCategoryId(restaurantId, categoryId)
    }


    fun warning(): LiveData<ApiResult<String?>> {
        return network.warning()
    }


}