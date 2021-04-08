package io.flaterlab.kyrgyzdaamy.service.response

import com.google.gson.annotations.SerializedName

data class ValidateDelivery(
        @SerializedName("comment")val comment: String,
        @SerializedName("deliveryAt")val deliverAt: String,
        @SerializedName("deliveryType")val deliveryType: Int
)