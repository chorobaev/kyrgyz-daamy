package com.timelysoft.amore.adapter.category

import com.timelysoft.amore.service.model2.response2.Category

interface CategoryListener {
    fun onCategoryClick(item: Category)
}