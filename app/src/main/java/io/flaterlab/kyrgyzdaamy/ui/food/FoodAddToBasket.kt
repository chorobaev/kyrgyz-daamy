package io.flaterlab.kyrgyzdaamy.ui.food

import io.flaterlab.kyrgyzdaamy.service.response.MenuItem

interface FoodAddToBasket {
    fun addToBasket(item: MenuItem, position: Int)
}