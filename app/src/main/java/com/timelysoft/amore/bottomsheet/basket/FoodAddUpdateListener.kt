package com.timelysoft.amore.bottomsheet.basket

import com.timelysoft.amore.service.response.MenuItem


interface FoodAddUpdateListener {
    fun addOrUpdateFoodBasket(hashMap: HashMap<Int, List<MenuItem>>, count: Int)
}