package com.timelysoft.amore.ui.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.timelysoft.amore.BasketCommands
import com.timelysoft.amore.service.ApiResult
import com.timelysoft.amore.service.NetworkRepositoryMod
import com.timelysoft.amore.service.model.BaseResponse
import com.timelysoft.amore.service.response.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class OrderViewModel @Inject constructor(private val network: NetworkRepositoryMod) :ViewModel() {
    fun cities(): LiveData<ApiResult<BaseResponse<List<CityRestResponse>>>> {
        return network.cities()
    }

    fun streets(id: Int): LiveData<ApiResult<BaseResponse<List<StreetResponse>>>> {
        return network.streets(id)
    }

    fun getBasketElements(): LiveData<HashMap<String,MenuItem>> {
        return BasketCommands.liveDataHashMap
    }

    fun orderValidate(orderModel: ValidateOrder): LiveData<ApiResult<BaseResponse<OrderValidateResponse>>> {
        return network.orderValidate(orderModel)

    }
    fun warning(): LiveData<ApiResult<BaseResponse<String>>> {
        return network.warning()
    }


}