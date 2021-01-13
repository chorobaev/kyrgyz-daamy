package com.timelysoft.kainarapp.ui.food

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.timelysoft.kainarapp.adapter.food.BasketCommands
import com.timelysoft.kainarapp.base.BaseViewModel
import com.timelysoft.kainarapp.service.ApiResult
import com.timelysoft.kainarapp.service.Resource
import com.timelysoft.kainarapp.service.ResultWrapper
import com.timelysoft.kainarapp.service.db.entity.BasketEntity
import com.timelysoft.kainarapp.service.db.entity.CategoryEntity
import com.timelysoft.kainarapp.service.db.entity.MenuItemEntity
import com.timelysoft.kainarapp.service.model2.BaseResponse
import com.timelysoft.kainarapp.service.model2.ErrorResponse
import com.timelysoft.kainarapp.service.model2.RestaurantResponse
import com.timelysoft.kainarapp.service.model2.ValidateGuest
import com.timelysoft.kainarapp.service.model2.response2.*
import com.timelysoft.kainarapp.service.response.*
import kotlinx.coroutines.launch

class FoodViewModel(application: Application) : BaseViewModel(application) {


    fun categories(): LiveData<List<CategoryEntity>> {
        return db.findAllCategories()
    }

    fun getExceptionOfPayment(token: String): LiveData<ApiResult<HashMap<String, List<String>>>> {
        return network.getExceptionMessage(token)
    }

    fun insertMenuItem(menuItem: MenuItem, index: Int, modGroup: List<BaseModifierGroup>) {
        BasketCommands.insertMenuItemSecondVersion(menuItem, index, modGroup)
    }

    fun deleteMenuItem(position: Int) {
        BasketCommands.deleteFromBasket(position)
    }

    fun insertMenuItemWithoutModifiers(
        menuItem: MenuItem,
        index: Int,
        list: List<BaseModifierGroup>
    ) {
        BasketCommands.insertMenuItemSecondVersion(menuItem, index, list)
    }

    fun basket(): LiveData<List<BasketEntity>> {
        return db.findAllBaskets()
    }

    fun getBasketElements(): LiveData<List<MenuItem>> {
        return BasketCommands.liveDataOfMenuItems
    }
    fun orderValidate(orderModel: ValidateOrder) : LiveData<ApiResult<OrderValidateResponse?>>{
        return network.orderValidate(orderModel)

    }

    fun orderCreate(orderModel: CreateOrder): LiveData<ApiResult<String?>> {
        return network.orderCreate(orderModel)
    }

    fun payOnline(
        restaurantId: String,
        orderId: String
    ): LiveData<ApiResult<OnlinePaymentResponse?>> {
        return network.onlinePayment(restaurantId, orderId)
    }

    //TODO("Ask about online payment")


    fun restaurants(): LiveData<ApiResult<List<RestaurantResponse>>> {
        return network.restaurants()
    }

    fun cities(): LiveData<ApiResult<List<CityRestResponse>?>> {
        return network.cities()
    }

    fun streets(id: Int): LiveData<ApiResult<List<StreetResponse>?>> {
        return network.streets(id)
    }

    fun categoriesByRestaurantId(id: String) : LiveData<ApiResult<BaseResponse<CategoriesResponse>?>> {
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

    fun listAddresses(): LiveData<ApiResult<ArrayList<ListAddressesResponse>>> {
        return networkCRM.addresses()
    }


}