package com.timelysoft.amore.service.model2.response2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class OrderValidateResponse(
    @SerializedName("amount") val amount: String,
    @SerializedName("discountSum") val discountSum : Int,
    @SerializedName("products") val products :List<ResponseProductOrderState>,
    @SerializedName("discounts") val discounts : List<Discount>
): Parcelable