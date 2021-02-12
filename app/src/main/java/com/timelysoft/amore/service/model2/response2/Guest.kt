package com.timelysoft.amore.service.model2.response2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.timelysoft.amore.service.model2.AddressInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Guest(
        @SerializedName("addressInfo") val addressInfo: AddressInfo?= null,
        @SerializedName("firstName") val firstName: String = "",
        @SerializedName("phoneNumber")val phoneNumber: String="",
        @SerializedName("surname")val surname: String=""
) : Parcelable