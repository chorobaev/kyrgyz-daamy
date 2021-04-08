package io.flaterlab.kyrgyzdaamy.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.flaterlab.kyrgyzdaamy.BasketCommands
import io.flaterlab.kyrgyzdaamy.service.ApiResult
import io.flaterlab.kyrgyzdaamy.service.NetworkRepositoryMod
import io.flaterlab.kyrgyzdaamy.service.model.BaseResponse
import io.flaterlab.kyrgyzdaamy.service.response.*
import javax.inject.Inject


@HiltViewModel
class OrderViewModel @Inject constructor(private val network: NetworkRepositoryMod) : ViewModel() {
    fun cities(): LiveData<ApiResult<BaseResponse<List<CityRestResponse>>>> {
        return network.cities()
    }

    fun streets(id: Int): LiveData<ApiResult<BaseResponse<List<StreetResponse>>>> {
        return network.streets(id)
    }

    fun getBasketElements(): LiveData<HashMap<String, MenuItem>> {
        return BasketCommands.liveDataHashMap
    }

    fun orderValidate(orderModel: ValidateOrder): LiveData<ApiResult<BaseResponse<OrderValidateResponse>>> {
        return network.orderValidate(orderModel)

    }

    fun warning(): LiveData<ApiResult<BaseResponse<String>>> {
        return network.warning()
    }
}