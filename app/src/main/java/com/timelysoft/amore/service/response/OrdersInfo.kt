package com.timelysoft.amore.service.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OrdersInfo (
    @SerializedName("TotalOrders")
    @Expose
    var totalOrders: Int = 0,

    @SerializedName("TotalSpent")
    @Expose
    var totalSpent: Int = 0,

    @SerializedName("LastOrderUtc")
    @Expose
    var lastOrderUtc: String = ""
)