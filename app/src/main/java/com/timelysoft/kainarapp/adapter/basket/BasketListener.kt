package com.timelysoft.kainarapp.adapter.basket


import com.timelysoft.kainarapp.service.model2.response2.MenuItem

interface BasketListener {
    fun onClickItem(item: MenuItem, position: Int)
    fun onDeleteItem(position : Int)
}