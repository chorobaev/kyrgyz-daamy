package io.flaterlab.kyrgyzdaamy.service.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ResponseProductOrderState(
    @SerializedName("code") val code: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("name") val name: String,
    @SerializedName("modifiers") val modifiers: List<ModifierOrderState> = ArrayList(),
    @SerializedName("discounted") val discounted : Int,
    @SerializedName("total") val total : Int,
    @SerializedName("price") val price: Int
) : Parcelable