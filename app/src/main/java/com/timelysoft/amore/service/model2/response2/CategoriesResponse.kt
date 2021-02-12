package com.timelysoft.amore.service.model2.response2

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(
        @SerializedName("categories")val categories: List<Category>,
        @SerializedName("logo")val logo: File,
        @SerializedName("restaurantName")val restaurantName: String,
        @SerializedName("version")val version: String
)