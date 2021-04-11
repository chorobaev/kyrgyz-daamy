package io.flaterlab.kyrgyzdaamy.service

import android.content.Context
import androidx.lifecycle.liveData
import dagger.hilt.android.qualifiers.ApplicationContext
import io.flaterlab.kyrgyzdaamy.R
import io.flaterlab.kyrgyzdaamy.service.response.CreateOrder
import io.flaterlab.kyrgyzdaamy.service.response.ValidateOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.io.IOException
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class NetworkRepositoryMod @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiService: ApiServiceMod
) {
    private val dispatcher = Dispatchers.IO


    fun getSchedules() = liveData(dispatcher) {
        emit(safeExecute { apiService.getSchedules() })
    }


    fun getMenuCategories(id: String) =

        liveData(dispatcher) {

            emit(ApiResult.Loading("loading"))
            emit(safeExecute { apiService.getCategoriesForRestaurant(id) })
        }


    fun restaurantById(restaurantId: String) = liveData(dispatcher) {

        emit(safeExecute {
            apiService.getRestaurantById(restaurantId)
        })
    }

    fun foodItemByCategoryId(categoryId: String) = liveData(dispatcher) {
        emit(ApiResult.Loading("loading"))
        emit(safeExecute { apiService.getItemsByCategory(categoryId) })

    }


    fun groupNews() = liveData(dispatcher) {

        emit(safeExecute {
            apiService.getRestraurantGroupInfo()
        })

    }

    fun news(restaurantId: String) = liveData(dispatcher) {
        emit(safeExecute {
            apiService.getRestaurantInfo(restaurantId)
        })

    }

    fun cities() =
        liveData(dispatcher) {
            emit(safeExecute { apiService.getCitiesForRes() })
        }

    fun streets(id: Int) =
        liveData(dispatcher) {
            emit(safeExecute { apiService.getStreetsForCities(id) })
        }

    fun orderValidate(orderModel: ValidateOrder) =
        liveData(dispatcher) {
            emit(safeExecute {
                apiService.validateOrder(AppPreferences.restaurant, orderModel)
            })
        }

    fun orderCreate(orderModel: CreateOrder) =
        liveData(dispatcher) {
            emit(safeExecute {
                apiService.createOrder(AppPreferences.restaurant, orderModel)
            })
        }

    fun onlinePayment(restaurantId: String, orderId: String) =
        liveData(dispatcher) {
            emit(safeExecute {
                apiService.getPaymentLink(restaurantId, orderId)
            })
        }

    fun warning() =
        liveData(dispatcher) {
            emit(safeExecute { apiService.warning() })
        }


    private inline fun <T> safeExecute(
        block: () -> Response<T>
    ): ApiResult<T> {
        return try {
            block().extractResponseBody()
        } catch (e: Exception) {
            if (e is IOException) {
                ApiResult.NetworkError(ErrorTypes.TimeOutError(context.getString(R.string.reason_timeout)))
            } else {
                ApiResult.NetworkError(ErrorTypes.ConnectionError(context.getString(R.string.reason_network)))
            }
        }
    }


    private fun <T> Response<T>.extractResponseBody() =
        if (isSuccessful) {
            body()?.let {
                ApiResult.Success(it)
            }
                ?: ApiResult.NetworkError(ErrorTypes.EmptyResultError(context.getString(R.string.reason_empty_body)))
        } else {
            ApiResult.Error(errorBody())
        }
}
