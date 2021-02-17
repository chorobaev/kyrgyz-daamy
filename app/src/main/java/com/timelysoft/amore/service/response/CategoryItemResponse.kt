package com.timelysoft.amore.service.response

import com.google.gson.annotations.SerializedName

data class CategoryItemResponse(
        @SerializedName("categoryName")val categoryName: String,
        @SerializedName("hashCode")val hashCode: Int,
        @SerializedName("items")val menuItems: List<MenuItem>
)