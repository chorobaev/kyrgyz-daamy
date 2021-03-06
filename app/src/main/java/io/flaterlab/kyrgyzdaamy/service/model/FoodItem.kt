package io.flaterlab.kyrgyzdaamy.service.model

data class FoodItem(
    val code : Int,
    val description : String?,
    val isHit : Int?,
    val modifierGroup: ItemModifierGroup,
    val name : String?,
    val price : Int?,
    val recipe :String?,
    val rests : Int?,
    val weight : Int?
)