package com.timelysoft.kainarapp.service.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderMenuItemDiscountResponse(
    val code: Int,
    val discounted: Int,
    val name: String,
    val price: Int,
    val quantity: Int,
    val total: Int
) : Parcelable