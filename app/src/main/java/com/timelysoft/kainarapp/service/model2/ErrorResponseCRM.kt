package com.timelysoft.kainarapp.service.model2

import com.google.gson.annotations.SerializedName

data class ErrorResponseCRM(
    @SerializedName("message") val message : String,
    @SerializedName("ModelState") val hashMap : HashMap<String, List<String>>
)