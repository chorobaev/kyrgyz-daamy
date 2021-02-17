package com.timelysoft.amore.adapter.food

import com.timelysoft.amore.service.response.MenuItem

interface FoodListener {

    fun onFoodClick(menuItem: MenuItem, position : Int)
}