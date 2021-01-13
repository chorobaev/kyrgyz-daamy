package com.timelysoft.kainarapp.service.model

import com.google.gson.annotations.SerializedName

class TokenModel(
    @SerializedName("refreshToken")
    var refreshToken: String,
    @SerializedName("accessToken")
    var accessToken: String
)