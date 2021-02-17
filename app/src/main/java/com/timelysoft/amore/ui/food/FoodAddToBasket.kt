package com.timelysoft.amore.ui.food

import com.timelysoft.amore.service.response.MenuItem

interface FoodAddToBasket {
    fun addToBasket(item :MenuItem, position : Int)
}