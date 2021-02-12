package com.timelysoft.amore.bottomsheet.basket

import com.timelysoft.amore.service.model2.response2.MenuItem


interface FoodAddUpdateListener {
    fun addOrUpdateFoodBasket(hashMap: HashMap<Int, List<MenuItem>>, count: Int)
}