package com.timelysoft.amore.service.model2

import com.google.gson.annotations.SerializedName

class Currency(
    @SerializedName("id") val id :String,
    @SerializedName("name") val name : String,
    @SerializedName("code") val code : String
)