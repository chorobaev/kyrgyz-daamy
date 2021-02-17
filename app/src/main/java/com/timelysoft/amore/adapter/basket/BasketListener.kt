package com.timelysoft.amore.adapter.basket


import com.timelysoft.amore.service.response.MenuItem

interface BasketListener {
    fun onClickItem(item: MenuItem, position: Int)
    fun onDeleteItem(position : Int)
}