package com.timelysoft.kainarapp.service.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ProfileAccountResponse (
    @SerializedName("OrdersInfo")
    @Expose
    var ordersInfo: OrdersInfo,

    @SerializedName("BonusPoints")
    @Expose
    var bonusPoints: Double,

    @SerializedName("PrepayPoints")
    @Expose
    var prepayPoints: Double,

    @SerializedName("CashbackId")
    @Expose
    var cashbackId: Int,

    @SerializedName("CashbackValue")
    @Expose
    var cashbackValue: Double,

    @SerializedName("CashbackName")
    @Expose
    var cashbackName: String
)