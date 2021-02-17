package com.timelysoft.amore.service.model

import com.google.gson.annotations.SerializedName
import com.timelysoft.amore.service.response.File

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