package com.timelysoft.amore.adapter.food

import com.timelysoft.amore.service.response.MenuItem

interface AddMenuItemsToBasket {
    fun addMenuItems(menuItem: MenuItem)
}