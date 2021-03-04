package com.timelysoft.amore.service

import androidx.lifecycle.liveData
import com.timelysoft.amore.service.model.AuthBody

import com.timelysoft.amore.service.response.CreateOrder
import com.timelysoft.amore.service.response.ValidateOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay


class NetworkRepositoryMod(
    private val apiService: ApiServiceMod,
    private val dispatcher: CoroutineDispatcher
) {

    private var error = "Проверьте интернет подключение"

    fun getToken(authBody: AuthBody) =
        liveData(dispatcher) {
            try {
                val tokenBody = apiService.getAccessToken(authBody)
                when {
                    tokenBody.isSuccessful -> emit(ApiResult.Success(tokenBody.body()?.data))
                    else -> {
                        emit(ApiResult.Error(tokenBody.errorBody()))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResult.NetworkError(error))
            }
        }

    fun getExceptionMessage(token: String) =
        liveData(dispatcher) {
            try {
                val exception = apiService.getException(token)
                when {
                    exception.isSuccessful -> emit(ApiResult.Success(exception.body()!!.errors))
                    else -> {
                        emit(ApiResult.Error(exception.errorBody()!!))
                    }

                }
            } catch (throwable: Exception) {
                emit(ApiResult.NetworkError(error))
            }
        }

    fun getSchedules() = liveData(dispatcher) {
        val response = apiService.getSchedules()
        try {
            if (response.isSuccessful) {
                emit(ApiResult.Success(response.body()!!.data))
            } else {
                emit(ApiResult.Error(response.errorBody()))
            }
        } catch (e: Exception) {
            emit(ApiResult.NetworkError(error))
        }
    }


    fun getMenuCategories(id: String) =

        liveData(dispatcher) {

            emit(ApiResult.Loading("loading"))
            try {
                val menu = apiService.getCategoriesForRestaurant(id)
                if (menu.isSuccessful) {
                    delay(1000)
                    emit(ApiResult.Success(menu.body()))
                } else {
                    emit(ApiResult.Error(menu.errorBody()))
                    println("dsdsdsdsdsds")
                }
            } catch (e: Exception) {
                emit(ApiResult.NetworkError(error))
            }
        }

    fun restaurants() =
        liveData(dispatcher) {
            try {
                val restaurants = apiService.restaurants()
                if (restaurants.isSuccessful) {
                    emit(ApiResult.Success(restaurants.body()!!.data))
                } else {
                    emit(ApiResult.Error(restaurants.errorBody()!!))
                }

            } catch (throwable: Exception) {
                emit(ApiResult.NetworkError(error))
            }
        }


    fun restaurantById(restaurantId: String) = liveData(dispatcher) {

        try {
            val response = apiService.getRestaurantById(restaurantId)
            when {
                response.isSuccessful -> emit(ApiResult.Success(response.body()!!))
                else -> {
                    emit(ApiResult.Error(response.errorBody()!!))
                }
            }
        } catch (exception: Exception) {
            emit(ApiResult.NetworkError(exception.message!!))
        }
    }

    fun foodItemByCategoryId(categoryId: String) = liveData(dispatcher) {
        try {
            emit(ApiResult.Loading("loading"))
            val response = apiService.getItemsByCategory(categoryId)
            when {
                response.isSuccessful -> {
                    delay(1000)
                    emit(ApiResult.Success(response.body()!!.data))
                }
                else -> {
                    emit(ApiResult.Error(response.errorBody()!!))
                }

            }
        } catch (e: Exception) {
            emit(ApiResult.NetworkError(e.printStackTrace().toString()))
        }
    }

    fun isOpen() = liveData(dispatcher) {
        try {
            val response = apiService.isOpen()
            if (response.isSuccessful){
                emit(ApiResult.Success(response.body()!!) )
            }else{
                emit(ApiResult.Error(response.errorBody()))
            }
        }catch (e:Exception){
            emit(ApiResult.NetworkError(error))
        }
    }

    fun groupNews() = liveData(dispatcher) {

        val response = apiService.getRestraurantGroupInfo()
        when {
            response.isSuccessful -> {
                emit(ApiResult.Success(response.body()))
            }
            else -> {
                emit(ApiResult.Error(response.errorBody()))
            }
        }

    }

    fun news(restaurantId: String) = liveData(dispatcher) {
        try {
            val response = apiService.getRestaurantInfo(restaurantId)
            when {
                response.isSuccessful -> {
                    emit(ApiResult.Success(response.body()))
                }
                else -> {
                    emit(ApiResult.Error(response.errorBody()))
                }
            }
        } catch (e: Exception) {
            emit(ApiResult.NetworkError(error))
        }

    }

    fun cities() =
        liveData(dispatcher) {
            try {
                val response =
                    apiService.getCitiesForRes()
                when {
                    response.isSuccessful -> {
                        emit(ApiResult.Success(response.body()?.data))
                    }
                    else -> {
                        emit(ApiResult.Error(response.errorBody()))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResult.NetworkError(error))
            }
        }

    fun streets(id: Int) =
        liveData(dispatcher) {
            try {
                val response =
                    apiService.getStreetsForCities(id)
                when {
                    response.isSuccessful -> {
                        emit(ApiResult.Success(response.body()?.data))
                    }
                    else -> {
                        emit(ApiResult.Error(response.errorBody()))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResult.NetworkError(error))
            }
        }

    fun orderValidate(orderModel: ValidateOrder) =
        liveData(dispatcher) {

            try {
                val response = apiService.validateOrder(AppPreferences.restaurant, orderModel)
                when {
                    response.isSuccessful -> {
                        emit(ApiResult.Success(response.body()?.data))
                    }
                    else -> {
                        emit(ApiResult.Error(response.errorBody()))
                    }

                }
            } catch (e: Exception) {
                emit(ApiResult.NetworkError(error))
            }
        }

    fun orderCreate(orderModel: CreateOrder) =
        liveData(dispatcher) {
            try {
                val response =
                    apiService.createOrder(AppPreferences.restaurant, orderModel)
                when {
                    response.isSuccessful -> {
                        emit(ApiResult.Success(response.body()?.data))
                    }
                    else -> {
                        emit(ApiResult.Error(response.errorBody()))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResult.NetworkError(error))
            }
        }

    fun onlinePayment(restaurantId: String, orderId: String) =
        liveData(dispatcher) {

            try {
                val response = apiService.getPaymentLink(restaurantId, orderId)
                when {
                    response.isSuccessful -> {
                        emit(ApiResult.Success(response.body()?.data))
                    }
                    else -> {
                        emit(ApiResult.Error(response.errorBody()))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResult.NetworkError(error))
            }
        }

    fun warning() =
        liveData(dispatcher) {
            try {
                val response =
                    apiService.warning()
                when {
                    response.isSuccessful -> {
                        emit(ApiResult.Success(response.body()?.data))
                    }
                    else -> {
                        emit(ApiResult.Error(response.errorBody()))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResult.NetworkError(error))
            }
        }
}
