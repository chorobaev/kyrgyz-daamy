package com.timelysoft.kainarapp.ui.food

import com.timelysoft.kainarapp.service.model2.response2.Category

interface OnExpandableAdapterClick {
    fun onItemClick(category: Category)
}