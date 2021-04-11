package io.flaterlab.kyrgyzdaamy.adapter.basket


import io.flaterlab.kyrgyzdaamy.service.response.MenuItem

interface BasketListener {
    fun onClickItem(item: MenuItem)
    fun onDeleteItem(item: MenuItem)
}