package com.timelysoft.amore.ui.food

import com.timelysoft.amore.service.response.Category

interface OnExpandableAdapterClick {
    fun onItemClick(category: Category)
}