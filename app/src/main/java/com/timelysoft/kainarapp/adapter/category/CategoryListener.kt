package com.timelysoft.kainarapp.adapter.category

import com.timelysoft.kainarapp.service.db.entity.CategoryEntity
import com.timelysoft.kainarapp.service.model2.response2.Category

interface CategoryListener {
    fun onCategoryClick(item: Category)
}