package com.timelysoft.amore.adapter.food

import com.timelysoft.amore.service.model2.response2.BaseModifier
import com.timelysoft.amore.service.model2.response2.BaseModifierGroup

interface AddToBasketListener {
    fun addToBasket(item : BaseModifier, baseModifierGroup: BaseModifierGroup)
}