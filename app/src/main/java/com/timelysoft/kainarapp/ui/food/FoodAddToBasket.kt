package com.timelysoft.kainarapp.ui.food

import com.timelysoft.kainarapp.service.model2.response2.MenuItem

interface FoodAddToBasket {
    fun addToBasket(item :MenuItem, position : Int)
}