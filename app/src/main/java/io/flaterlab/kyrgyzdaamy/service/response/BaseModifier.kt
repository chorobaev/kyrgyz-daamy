package io.flaterlab.kyrgyzdaamy.service.response

import com.google.gson.annotations.SerializedName

data class BaseModifier(
    @SerializedName("ident") val ident : Int,
    @SerializedName("name") val name :String,
    @SerializedName("code") val code: Int,
    @SerializedName("price") var price :Int?=null,
    @SerializedName("limit") val limit :Int,
    var isChecked : Boolean = false,
    var count : Int
){
    constructor():this(0,"",0,null,1,false,0)
}