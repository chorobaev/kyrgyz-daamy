package com.timelysoft.amore.service.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderResponse(
    val amount: Int,
    val discounts: List<OrderDiscountResponse>,
    val discountsum: Int,
    val id: Int,
    val items: List<OrderMenuItemDiscountResponse>,
    val paid: Int,
    val status: Int,
    var statusValue: String = ""
) : Parcelable