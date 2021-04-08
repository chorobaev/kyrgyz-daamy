package io.flaterlab.kyrgyzdaamy.adapter.food

import io.flaterlab.kyrgyzdaamy.service.response.MenuItem

interface AddMenuItemsToBasket {
    fun addMenuItems(menuItem: MenuItem)
}