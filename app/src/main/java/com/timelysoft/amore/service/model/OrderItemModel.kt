package com.timelysoft.amore.service.model

import com.google.gson.annotations.SerializedName

class OrderItemModel (
    @SerializedName("Id") val id : Int,
    @SerializedName("RkOrderNum") val rkOrderNum : String,
    @SerializedName("RkOrderDate") val rkOrderDate : String,
    @SerializedName("OrderType") val orderType : Int,
    @SerializedName("PayType") val payType : Int,
    @SerializedName("Sum") val sum : Int,
    @SerializedName("IsCanceled") val isCanceled : Boolean
    )