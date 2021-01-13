package com.timelysoft.kainarapp.service.model2.response2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderStateResponse(
        @SerializedName("amount")val amount: Int,
        @SerializedName("discountSum")val discountSum: Int,
        @SerializedName("discounts")val discounts: List<Discount> = ArrayList(),
        @SerializedName("id")val id: Int,
        @SerializedName("paid")val paid: Int,
        @SerializedName("products")val products: List<ResponseProductOrderState> = ArrayList(),
        @SerializedName("status")val status: Int
):Parcelable