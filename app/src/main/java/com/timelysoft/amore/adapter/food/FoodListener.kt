package com.timelysoft.amore.adapter.food

import com.timelysoft.amore.service.model2.response2.MenuItem

interface FoodListener {

    fun onFoodClick(menuItem: MenuItem, position : Int)
}