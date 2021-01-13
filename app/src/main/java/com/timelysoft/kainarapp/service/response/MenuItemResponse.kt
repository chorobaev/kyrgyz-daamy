package com.timelysoft.kainarapp.service.response

data class MenuItemResponse(
    val categpath: String,
    val code: Int,
    val image: String,
    val name: String,
    val price: Int,
    val recipe: String,
    val rests: Int
)