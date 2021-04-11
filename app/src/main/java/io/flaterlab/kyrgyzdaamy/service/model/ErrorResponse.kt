package io.flaterlab.kyrgyzdaamy.service.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse (
    @SerializedName("type")
    val type : Int,
    @SerializedName("status")
    val status: Int,
    @SerializedName("errors")
    val errors: HashMap<String, List<String>>
)