package com.timelysoft.kainarapp.service.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDiscountResponse(
    val code: Int,
    val name: String,
    val total: Double
) : Parcelable