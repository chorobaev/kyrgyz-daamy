package com.timelysoft.amore.service.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CreateOrder(
        @SerializedName("guest")val guest: Guest?,
        @SerializedName("order")val order: Order
) :Parcelable