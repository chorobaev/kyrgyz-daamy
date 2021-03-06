package io.flaterlab.kyrgyzdaamy.service.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
        @SerializedName("code")val code: Int,
        @SerializedName("count")val count: Int,
        @SerializedName("name")val name: String,
        @SerializedName("modifiers")val modifiers: List<Modifier> = ArrayList(),
       // @SerializedName("discounted") var discounted : Int = 0,
        @SerializedName("total") var total : Int = 0,
        @SerializedName("price") var price :Int = 0

) : Parcelable