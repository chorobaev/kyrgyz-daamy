package com.timelysoft.kainarapp.service.model2

import com.google.gson.annotations.SerializedName
import com.timelysoft.kainarapp.service.model2.response2.File

data class Restaurant(
        @SerializedName("cityId")val cityId: Int,
        @SerializedName("crmId")val crmId: Int,
        @SerializedName("currencyId")val currencyId: String,
        @SerializedName("id")val id: String,

        @SerializedName("logo")val logo: File?,
        @SerializedName("name")val name: String,
        @SerializedName("onlinePaymentSupported")val onlinePaymentSupported: Boolean,
        @SerializedName("restaurantGroupId")val restaurantGroupId: String,
        @SerializedName("userId")val userId: String
)