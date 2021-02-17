package com.timelysoft.amore.ui.food

import com.timelysoft.amore.service.response.Category

interface OnChildItemListener {

    fun onChildClick(category: Category)
}