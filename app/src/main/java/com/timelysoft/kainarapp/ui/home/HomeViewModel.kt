package com.timelysoft.kainarapp.ui.home


import android.app.Application
import androidx.lifecycle.GeneratedAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.timelysoft.kainarapp.App
import com.timelysoft.kainarapp.base.BaseViewModel
import com.timelysoft.kainarapp.service.ApiResult
import com.timelysoft.kainarapp.service.AppPreferences
import com.timelysoft.kainarapp.service.Resource
import com.timelysoft.kainarapp.service.ResultWrapper
import com.timelysoft.kainarapp.service.model2.BaseResponse
import com.timelysoft.kainarapp.service.model2.ErrorResponse
import com.timelysoft.kainarapp.service.model2.RestaurantResponse
import com.timelysoft.kainarapp.service.model2.response2.CategoriesResponse
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : BaseViewModel(application) {


    fun restaurants(): LiveData<ApiResult<List<RestaurantResponse>>> {
        return network.restaurants()
    }

}