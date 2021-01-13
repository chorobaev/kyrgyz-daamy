package com.timelysoft.kainarapp.bottomsheet.basket

import com.timelysoft.kainarapp.service.model2.ItemModifier
import com.timelysoft.kainarapp.service.model2.response2.BaseModifierGroup
import com.timelysoft.kainarapp.service.model2.response2.MenuItem


interface FoodAddUpdateListener {
    fun addOrUpdateFoodBasket(hashMap: HashMap<Int, List<MenuItem>>, count: Int)
}