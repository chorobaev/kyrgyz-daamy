package com.timelysoft.amore.service.model2

import com.google.gson.annotations.SerializedName

data class ErrorResponse (
    @SerializedName("type")
    val type : Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("errors")
    val errors: HashMap<String, List<String>>
)