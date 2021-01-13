package com.timelysoft.kainarapp.ui.history

import android.app.Application
import androidx.lifecycle.LiveData
import com.timelysoft.kainarapp.base.BaseViewModel
import com.timelysoft.kainarapp.service.ApiResult
import com.timelysoft.kainarapp.service.Resource
import com.timelysoft.kainarapp.service.model2.Restaurant
import com.timelysoft.kainarapp.service.model2.RestaurantResponse
import com.timelysoft.kainarapp.service.response.*

class HistoryViewModel(application: Application) : BaseViewModel(application) {

    fun restaurants(): LiveData<ApiResult<List<RestaurantResponse>>> {
        return network.restaurants()
    }

    fun orders(
        page: Int,
        pageSize: Int,
        restaurantId: Int
    ): LiveData<ApiResult<HistoryOrdersResponse>> {
        return networkCRM.historyOrderItems(page, pageSize, restaurantId)
    }

    fun orderDetail(id: Int): LiveData<ApiResult<HistoryOrderDetailResponse>> {
        return networkCRM.historyOrderDetail(id)
    }

    /*
    fun checkLastOrder(
        restaurantgroupid: String,
        orderId: String
    ): LiveData<Resource<CommonResponse<OrderResponse>>> {
        return network.checkLastOrder(restaurantgroupid, orderId)
//        return network.checkLastOrderDemirBank(restaurantgroupid, "2")
    }



    fun checkLastOrderDemir(paymentid: String): LiveData<Resource<CommonResponse<OrderResponse>>> {
        return network.checkLastOrderDemir(paymentid)
    }


    fun orderStatuses(): LiveData<Resource<ArrayList<OrderType>>> {
        return network.orderStatuses()
    }

     */

}