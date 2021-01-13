package com.timelysoft.kainarapp.adapter.food

import com.timelysoft.kainarapp.service.model2.ItemModifier
import com.timelysoft.kainarapp.service.model2.response2.BaseModifier
import com.timelysoft.kainarapp.service.model2.response2.BaseModifierGroup

interface AddToBasketListener {
    fun addToBasket(item : BaseModifier, baseModifierGroup: BaseModifierGroup)
}