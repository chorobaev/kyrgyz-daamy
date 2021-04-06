package com.timelysoft.amore.adapter.basket


import com.timelysoft.amore.service.response.MenuItem

interface BasketListener {
    fun onClickItem(item: MenuItem)
    fun onDeleteItem(item: MenuItem)
}