package com.timelysoft.kainarapp.service.model2.response2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.timelysoft.kainarapp.service.model2.AddressInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GuestValidate(
    @SerializedName("phoneNumber")val phoneNumber: String,
    @SerializedName("firstName") val name : String,
    @SerializedName("surname") val lastName : String,
    @SerializedName("middleName") val middleName: String,
    @SerializedName("addressInfo") val addressInfo: AddressInfo
) : Parcelable