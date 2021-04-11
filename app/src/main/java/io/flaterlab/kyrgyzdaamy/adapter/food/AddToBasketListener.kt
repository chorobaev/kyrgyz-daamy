package io.flaterlab.kyrgyzdaamy.adapter.food

import io.flaterlab.kyrgyzdaamy.service.response.BaseModifier
import io.flaterlab.kyrgyzdaamy.service.response.BaseModifierGroup

interface AddToBasketListener {
    fun addToBasket(item: BaseModifier, baseModifierGroup: BaseModifierGroup)
}