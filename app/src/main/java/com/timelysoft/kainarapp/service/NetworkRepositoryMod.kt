package com.timelysoft.kainarapp.service

import androidx.lifecycle.liveData
import com.timelysoft.kainarapp.extension.toMyDate

import com.timelysoft.kainarapp.service.model2.response2.CreateOrder
import com.timelysoft.kainarapp.service.model2.response2.ValidateOrder
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


class NetworkRepositoryMod(
    private val apiService: ApiServiceMod,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private var error = "Ошибка при получении данных"


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


    fun getMenuCategories(id: String) =

        liveData(dispatcher) {
            try {
                val menuVersion = apiService.getMenuVersionForRes(id)

                when {
                    menuVersion.isSuccessful -> {
                        val a = AppPreferences.version()

                        if (a != menuVersion.body()?.data) {
                            val menu =
                                apiService.getCategoriesForRestaurant(id)
                            if (menu.isSuccessful) {
                                emit(ApiResult.Success(menu.body()))
                            } else {
                                emit(ApiResult.Error(menu.errorBody()))
                            }
                        }
                    }
                    else -> {
                        val menu = apiService.getCategoriesForRestaurant(id)
                        if (menu.isSuccessful) {
                            emit(ApiResult.Success(menu.body()))
                        } else {
                            emit(ApiResult.Error(menu.errorBody()))
                        }
                    }
                }
            } catch (e: Exception) {
                emit(ApiResult.NetworkError(error))
            }
        }

    fun restaurants() =
        liveData(dispatcher) {
            try {
                val restaurants = apiService.restaurants(AppPreferences.group())
                when {
                    restaurants.isSuccessful -> emit(ApiResult.Success(restaurants.body()!!.data))
                    else -> {
                        ApiResult.Error(restaurants.errorBody()!!)
                    }
                }

            } catch (throwable: Exception) {
                emit(ApiResult.NetworkError(throwable.message!!))
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

    fun foodItemByCategoryId(restaurantId: String, categoryId: String) = liveData(dispatcher) {
        try {
            val response = apiService.getItemsByCategory(restaurantId, categoryId)

            when {
                response.isSuccessful -> {
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


    fun restaurantsCRM() = liveData(dispatcher) {
        try {
            val response =
                apiService.restaurantsСRM()
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
            val response = apiService.validateOrder(AppPreferences.restaurant, orderModel)
            try {
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
                val response = apiService.createOrderOnline(restaurantId, orderId)
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
