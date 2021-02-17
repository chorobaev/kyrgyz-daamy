package com.timelysoft.amore.service.model

import com.google.gson.annotations.SerializedName

data class RestaurantDetail(
        @SerializedName("about")val about: String,
        @SerializedName("address")val address: String,
        @SerializedName("onlineAddress")val onlineAddress: String,
        @SerializedName("refundReturnPolicy")val refundReturnPolicy: String
)