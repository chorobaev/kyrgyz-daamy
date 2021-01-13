package com.timelysoft.kainarapp.service.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ListAddressesResponse (
    @SerializedName("StreetName")
    @Expose
    var streetName: String,

    @SerializedName("CityName")
    @Expose
    var cityName: String,

    @SerializedName("Id")
    @Expose
    var id: Int,

    @SerializedName("Building")
    @Expose
    var building: String

){
    override fun toString(): String {
        return "${cityName} ${streetName} ${building}"
    }
}