package com.timelysoft.amore.service.model

import com.google.gson.annotations.SerializedName


data class RestaurantGroupResponse(
        @SerializedName("id")val id: String,
        @SerializedName("name")val name: String,
        @SerializedName("restaurants")val restaurants: List<Restaurant>,
        @SerializedName("userId")val userId: String
)