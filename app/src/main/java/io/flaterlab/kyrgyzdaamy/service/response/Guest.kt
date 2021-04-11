package io.flaterlab.kyrgyzdaamy.service.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import io.flaterlab.kyrgyzdaamy.service.model.AddressInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Guest(
    @SerializedName("addressInfo") val addressInfo: AddressInfo? = null,
    @SerializedName("firstName") val firstName: String = "",
    @SerializedName("phoneNumber") val phoneNumber: String = "",
    @SerializedName("surname") val surname: String = ""
) : Parcelable