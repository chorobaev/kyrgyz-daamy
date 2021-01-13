package com.timelysoft.kainarapp.service.model2.response2

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModifierOrderState(
        @SerializedName("code")val code: Int,
        @SerializedName("count")val count: Int,
        @SerializedName("name")val name: String
):Parcelable