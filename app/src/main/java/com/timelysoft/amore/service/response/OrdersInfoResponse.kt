package com.timelysoft.amore.service.response

data class OrdersInfoResponse(
    val LastOrderUtc: String,
    val TotalOrders: Int,
    val TotalSpent: Int
)