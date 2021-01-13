package com.timelysoft.kainarapp.service.response

data class OrdersInfoResponse(
    val LastOrderUtc: String,
    val TotalOrders: Int,
    val TotalSpent: Int
)