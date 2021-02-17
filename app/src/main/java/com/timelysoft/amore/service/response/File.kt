package com.timelysoft.amore.service.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class File(
        @SerializedName("id")val id: String,
        @SerializedName("relativeUrl")val relativeUrl: String
) : Parcelable