package com.timelysoft.kainarapp.service

import androidx.lifecycle.liveData
import com.timelysoft.kainarapp.service.model.FirebaseTokenModel
import com.timelysoft.kainarapp.service.model.RegisterModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class NetworkRepositoryCRM
    (
    private val apiServiceCRM: ApiServiceCRM,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val error = "Ошибка при получении данных"

    fun register(register: RegisterModel) =

        liveData(dispatcher) {

            val response = apiServiceCRM.register(register)

            when {
                response.isSuccessful -> {
                    emit(ApiResult.Success(response.body()!!))
                }
                else -> {
                    emit(ApiResult.Error(response.errorBody()!!))
                }
            }
        }

    fun historyOrderDetail(id: Int) =
        liveData(dispatcher) {
            try {
                val response =
                    apiServiceCRM.historyOrderDetail(id)
                when {
                    response.isSuccessful -> {
                        emit(ApiResult.Success(response.body()!!))
                    }
                    else -> {
                        emit(ApiResult.Error(response.errorBody()!!))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResult.NetworkError(error))
            }
        }

    fun historyOrderItems(page: Int, pageSize: Int, restaurantId: Int) =
        liveData(dispatcher) {
            try {
                val response =
                    apiServiceCRM.historyOrder(page, pageSize, restaurantId)
                when {
                    response.isSuccessful -> {
                        emit(ApiResult.Success(response.body()!!))
                    }
                    else -> {
                        emit(ApiResult.Error(response.errorBody()!!))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResult.NetworkError(error))
            }

        }

    fun sendSms(phone: String) = liveData(dispatcher) {

        try {
            val response =
                apiServiceCRM.sendSms(phone)
            when {
                response.isSuccessful -> {
                    emit(ApiResult.Success(true))
                }
                else -> {
                    emit(ApiResult.Error(response.errorBody()!!))
                }
            }
        } catch (e: Exception) {
            emit(ApiResult.NetworkError(error))
        }

    }

    fun addresses() = liveData(dispatcher) {
        try {
            val response =
                apiServiceCRM.listAddresses()
            when {
                response.isSuccessful -> {
                    emit(ApiResult.Success(response.body()!!))
                }
                else -> {
                    emit(ApiResult.Error(response.errorBody()!!))
                }
            }
        } catch (e: Exception) {
            emit(ApiResult.NetworkError(error))
        }

    }

    fun auth(map: HashMap<String, String>) = liveData(dispatcher) {
        try {

            val response = apiServiceCRM.auth(map)
            when {
                response.isSuccessful -> {
                    emit(ApiResult.Success(response.body()!!))
                }
                else -> {
                    emit(ApiResult.Error(response.errorBody()!!))
                }
            }
        } catch (e: Exception) {
            emit(ApiResult.NetworkError(error))
        }
    }

    fun profile() = liveData(dispatcher) {

        try {
            val response =
                apiServiceCRM.profile()
            when {
                response.isSuccessful -> {
                    emit(ApiResult.Success(response.body()!!))
                }
                else -> {
                    emit(ApiResult.Error(response.errorBody()!!))
                }
            }
        } catch (e: Exception) {
            emit(ApiResult.NetworkError(error))
        }
    }

    fun profileAccount(id: Int) = liveData(dispatcher) {

        try {
            val response =
                apiServiceCRM.profileAccount(id)
            when {
                response.isSuccessful -> {
                    emit(ApiResult.Success(response.body()!!))
                }
                else -> {
                    emit(ApiResult.Error(response.errorBody()!!))
                }
            }
        } catch (e: Exception) {
            emit(ApiResult.NetworkError(error))
        }
    }

    fun sendToken(firebaseTokenModel: FirebaseTokenModel) = liveData(dispatcher) {
        try {
            val response =
                RetrofitClient.apiServiceCRM().sendFirebaseToken(firebaseTokenModel)
            if (response.isSuccessful){
                emit(ApiResult.Success(response.body()))
            }else{
                emit(ApiResult.Error(response.errorBody()))
            }
        } catch (e: Exception) {
            emit(ApiResult.NetworkError(""))
        }
    }

}