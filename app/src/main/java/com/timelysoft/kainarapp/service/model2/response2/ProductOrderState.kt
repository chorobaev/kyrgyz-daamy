package com.timelysoft.kainarapp.service.model2.response2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductOrderState(
    @SerializedName("code") val code: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("name") val name: String,
    @SerializedName("total") val total : Int = 0,
    @SerializedName("price") val price :Int = 0,
    @SerializedName("modifiers") val modifiers: List<ModifierOrderState> = ArrayList()
) : Parcelable