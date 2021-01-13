package com.timelysoft.kainarapp.adapter.restaurant

interface RestaurantListener {
    fun onRestaurantClick(
        restaurantId: String,
        photo: String = "",
        logo: String = "",
        crmId: Int
    )
}