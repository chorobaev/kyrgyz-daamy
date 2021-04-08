package io.flaterlab.kyrgyzdaamy.service.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsResponse(
        @SerializedName("boldDescription")val boldDescription: String,
        @SerializedName("description") val description: String,
        @SerializedName("endDateUtc")val endDateUtc: String,
        @SerializedName("files")val files: List<File>,
        @SerializedName("id")val id: String,
        @SerializedName("isShowOnTop")val isShowOnTop: Boolean,
        @SerializedName("name")val name: String,
        @SerializedName("restaurantId")val restaurantId: String,
        @SerializedName("restaurantGroupId") val restaurantGroupId : String,
        @SerializedName("startDateUtc")val startDateUtc: String,
        @SerializedName("type")val type: Int
) :Parcelable