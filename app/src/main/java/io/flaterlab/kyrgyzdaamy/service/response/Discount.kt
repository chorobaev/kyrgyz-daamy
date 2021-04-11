package io.flaterlab.kyrgyzdaamy.service.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Discount(
        @SerializedName("code")val code: Int,
        @SerializedName("name")val name: String,
        @SerializedName("total")val total: Int
):Parcelable