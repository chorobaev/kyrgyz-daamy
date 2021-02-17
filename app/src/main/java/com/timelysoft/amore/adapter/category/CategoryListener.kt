package com.timelysoft.amore.adapter.category

import com.timelysoft.amore.service.response.Category

interface CategoryListener {
    fun onCategoryClick(item: Category)
}