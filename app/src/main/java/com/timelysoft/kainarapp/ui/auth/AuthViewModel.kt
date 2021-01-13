package com.timelysoft.kainarapp.ui.auth

import android.app.Application
import androidx.lifecycle.LiveData
import com.timelysoft.kainarapp.base.BaseViewModel
import com.timelysoft.kainarapp.service.ApiResult
import com.timelysoft.kainarapp.service.Resource
import com.timelysoft.kainarapp.service.model.FirebaseTokenModel
import com.timelysoft.kainarapp.service.model.RegisterModel
import com.timelysoft.kainarapp.service.model2.BaseResponse
import com.timelysoft.kainarapp.service.model2.RestaurantResponseCRM
import com.timelysoft.kainarapp.service.response.*
class AuthViewModel(application: Application) : BaseViewModel(application) {


    fun register(register: RegisterModel): LiveData<ApiResult<Any>> {
        return networkCRM.register(register)
    }


    fun sendSms(phone: String): LiveData<ApiResult<Boolean>> {
        return networkCRM.sendSms(phone)
    }


    fun restaurantsCRM(): LiveData<ApiResult<List<RestaurantResponseCRM>?>> {
        return network.restaurantsCRM()
    }


    fun profile(): LiveData<ApiResult<ProfilessResponse>> {
        return networkCRM.profile()
    }

    fun profileAccount(id: Int): LiveData<ApiResult<ProfileAccountResponse>>{
        return networkCRM.profileAccount(id)
    }

    fun auth(phone: String, code: String): LiveData<ApiResult<AuthResponse>> {
        val map = HashMap<String, String>()
        map["username"] = phone
        map["password"] = code
        map["grant_type"] = "password"
        map["refresh_token"] = ""
        return networkCRM.auth(map)
    }

    fun sendToken(firebaseTokenModel: FirebaseTokenModel): LiveData<ApiResult<Unit?>> {
        return networkCRM.sendToken(firebaseTokenModel)
    }
}