package com.timelysoft.amore.service.model2.response2

import com.google.gson.annotations.SerializedName

data class BaseModifier(
    @SerializedName("ident") val ident : Int,
    @SerializedName("name") val name :String,
    @SerializedName("code") val code: Int,
    @SerializedName("price") var price :Int?=null,
    @SerializedName("limit") val limit :Int,
    var isChecked : Boolean = false,
    var count : Int
)