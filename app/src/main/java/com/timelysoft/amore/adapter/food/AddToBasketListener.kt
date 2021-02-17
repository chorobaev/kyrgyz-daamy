package com.timelysoft.amore.adapter.food

import com.timelysoft.amore.service.response.BaseModifier
import com.timelysoft.amore.service.response.BaseModifierGroup

interface AddToBasketListener {
    fun addToBasket(item : BaseModifier, baseModifierGroup: BaseModifierGroup)
}