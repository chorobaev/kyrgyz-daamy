package com.timelysoft.amore.service.response

import com.google.gson.annotations.SerializedName
import com.timelysoft.amore.service.model.OrderItemModel

class HistoryOrdersResponse (
    @SerializedName("TotalCount") val totalCount : Int,
    @SerializedName("Items") val items : List<OrderItemModel>,
    @SerializedName("Page") val page : Int,
    @SerializedName("PageSize") val pageSize : Int)