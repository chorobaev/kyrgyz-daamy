package com.timelysoft.kainarapp.service.model2.response2

import com.google.gson.annotations.SerializedName

data class ModifierValidate(
        @SerializedName("code")val code: Int,
        @SerializedName("count")val count: Int,
        @SerializedName("name")val name: String
)