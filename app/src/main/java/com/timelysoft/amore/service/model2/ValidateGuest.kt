package com.timelysoft.amore.service.model2

import com.google.gson.annotations.SerializedName

data class ValidateGuest(
        @SerializedName("phoneNumber") val phoneNumber : String,
        @SerializedName("firstName") val firstName : String,
        @SerializedName("surname") val surname: String,
        @SerializedName("middleName") val middleName:String
)