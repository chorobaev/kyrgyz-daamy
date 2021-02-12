package com.timelysoft.amore.service.model2.response2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ValidateOrder(
        @SerializedName("guest")val guest: GuestValidate,
        @SerializedName("order")val order: OrderValidate
) : Parcelable