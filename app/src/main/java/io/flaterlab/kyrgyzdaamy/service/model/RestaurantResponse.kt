package io.flaterlab.kyrgyzdaamy.service.model

import com.google.gson.annotations.SerializedName

data class RestaurantResponse(
    val id: String,
    @SerializedName("cityId") val cityId: Int,
    @SerializedName("currency") val currency: String,
    @SerializedName("name") val name: String,
    @SerializedName("onlinePaymentSupported") val onlinePaymentSupported: Boolean,
    @SerializedName("restaurantDetail") val restaurantDetail: RestaurantDetail
){
    constructor():this("",0,"","",false,RestaurantDetail())
}