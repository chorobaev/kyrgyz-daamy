package com.timelysoft.amore.ui.food

import com.timelysoft.amore.service.model2.response2.Category

interface OnExpandableAdapterClick {
    fun onItemClick(category: Category)
}