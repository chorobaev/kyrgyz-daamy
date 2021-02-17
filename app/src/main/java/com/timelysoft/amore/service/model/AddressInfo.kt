package com.timelysoft.amore.service.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class AddressInfo(
        @SerializedName("id")val id: Int,
        @SerializedName("cityId") var cityId: Int,
        @SerializedName("streetId")val streetId: Int,
        @SerializedName("building")val building: String,
        @SerializedName("entry")val entry: String,
        @SerializedName("entryCode")val entryCode: String,
        @SerializedName("floor")val floor: String,
        @SerializedName("apartment")val apartment: String,
        @SerializedName("comment")val comment: String,
        @SerializedName("isPrimary")val isPrimary : Boolean = true
) : Parcelable

