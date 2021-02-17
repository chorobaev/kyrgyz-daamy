package com.timelysoft.amore.service.model

data class ItemModifierGroup(
    val id : Int,
    val maximumSelected : Int,
    val minimumSelected : Int,
    val modifiers: List<ItemModifier>,
    val changesPrice : Boolean,
    val freeCount : Int
)