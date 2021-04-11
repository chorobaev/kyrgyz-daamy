package io.flaterlab.kyrgyzdaamy.bottomsheet.basket

import io.flaterlab.kyrgyzdaamy.service.response.MenuItem


interface FoodAddUpdateListener {
    fun addOrUpdateFoodBasket(hashMap: HashMap<Int, List<MenuItem>>, count: Int)
}