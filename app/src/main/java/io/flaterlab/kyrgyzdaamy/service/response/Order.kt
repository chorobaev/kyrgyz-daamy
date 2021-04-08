package io.flaterlab.kyrgyzdaamy.service.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
        @SerializedName("comment") var comment: String? =null,
        @SerializedName("deliverAt") var deliverAt: String? = null,
        @SerializedName("deliveryType") var deliveryType: Int? = null,
        @SerializedName("deviceType")val deviceType: Int = 2,
        @SerializedName("discountType") var discountType: Int? = null,
        @SerializedName("paymentType") var paymentType: Int? =null,
        @SerializedName("products") var products: List<Product> = ArrayList()
):Parcelable