package com.timelysoft.amore.ui.food

import com.timelysoft.amore.service.model2.response2.MenuItem

interface FoodAddToBasket {
    fun addToBasket(item :MenuItem, position : Int)
}