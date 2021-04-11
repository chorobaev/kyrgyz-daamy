package io.flaterlab.kyrgyzdaamy.extension

import io.flaterlab.kyrgyzdaamy.service.response.BaseModifierGroup


fun countCost(count: Int, menuItemPrice: Int, sumOfPrices: Int): Int {
    return (count * menuItemPrice + sumOfPrices)
}

fun countPriceOfMod(baseModifierGroup: BaseModifierGroup): Int {
    var sumOfPricesOfModifiers = 0
    var freeCount = baseModifierGroup.freeCount
    if (baseModifierGroup.changesPrice) {
        baseModifierGroup.modifiersList.values.forEachIndexed { index, baseModifier ->
            val countOfModifier = baseModifier.count
            for (i in 0 until countOfModifier) {
                if (freeCount > 0) {
                    freeCount -= 1
                } else {
                    sumOfPricesOfModifiers += baseModifier.price!!
                }
            }
        }
    }
    return sumOfPricesOfModifiers
}