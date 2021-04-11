package io.flaterlab.kyrgyzdaamy.adapter.food

import io.flaterlab.kyrgyzdaamy.service.response.MenuItem

interface FoodListener {

    fun onFoodClick(menuItem: MenuItem, position : Int)
}