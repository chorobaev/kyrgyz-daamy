package io.flaterlab.kyrgyzdaamy.service.response

import com.google.gson.annotations.SerializedName

data class ProductValidate(
        @SerializedName("code")val code: Int,
        @SerializedName("count")val count: Int,
        @SerializedName("modifiers")val modifiers: List<ModifierValidate>,
        @SerializedName("name")val name: String
)