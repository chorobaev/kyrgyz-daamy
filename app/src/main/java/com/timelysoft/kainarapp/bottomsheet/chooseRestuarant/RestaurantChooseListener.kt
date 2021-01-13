package com.timelysoft.kainarapp.bottomsheet.chooseRestuarant

interface RestaurantChooseListener {
    //fun onClickRestaurant(restaurantId: Int)
    fun onClickRestaurant(restaurantId: String, previousRestaurantId : String, crmId: Int)
}
