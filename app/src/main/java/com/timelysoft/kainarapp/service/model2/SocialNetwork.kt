package com.timelysoft.kainarapp.service.model2

import com.google.gson.annotations.SerializedName

data class SocialNetwork(
        @SerializedName("id")val id: Int,
        @SerializedName("type")val type: Int,
        @SerializedName("url")val url: String
)