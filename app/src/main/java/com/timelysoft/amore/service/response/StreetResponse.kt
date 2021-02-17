package com.timelysoft.amore.service.response

import com.google.gson.annotations.SerializedName

data class StreetResponse(
        @SerializedName("cityName")val cityName: String,
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String

){
        override fun toString(): String {
                return name
        }
}