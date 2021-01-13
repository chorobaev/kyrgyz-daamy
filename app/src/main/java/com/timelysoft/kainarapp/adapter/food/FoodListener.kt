package com.timelysoft.kainarapp.adapter.food

import com.timelysoft.kainarapp.service.model2.response2.MenuItem
import retrofit2.http.POST

interface FoodListener {

    fun onFoodClick(menuItem: MenuItem, position : Int)
}