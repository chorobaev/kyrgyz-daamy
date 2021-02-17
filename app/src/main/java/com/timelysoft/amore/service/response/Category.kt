package com.timelysoft.amore.service.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category (
        @SerializedName("id")
        val id: String,
        @SerializedName("hashCode")
        val hashCode: Long,
        @SerializedName("name")
        val name: String,
        @SerializedName("hasProducts")
        val hasProducts : Boolean,
        @SerializedName("categories")
        val categories : List<Category>?
) : Parcelable