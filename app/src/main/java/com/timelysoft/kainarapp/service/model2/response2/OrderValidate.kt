package com.timelysoft.kainarapp.service.model2.response2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class OrderValidate(
        @SerializedName("deliveryType") var deliveryType: Int?=null,
        @SerializedName("deviceType")val deviceType: Int = 1,
        @SerializedName("discountType") var discountType: Int?=null,
        @SerializedName("products") var products: List<ProductOrderState> = ArrayList(),
        @SerializedName("deliverAt") var deliverAt : String?,
        @SerializedName("paymentType") var paymentType : Int = 1
) : Parcelable