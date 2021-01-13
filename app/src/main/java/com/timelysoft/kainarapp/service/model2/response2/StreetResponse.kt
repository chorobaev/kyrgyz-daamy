package com.timelysoft.kainarapp.service.model2.response2

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