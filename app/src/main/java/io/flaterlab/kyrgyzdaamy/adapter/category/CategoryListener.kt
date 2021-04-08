package io.flaterlab.kyrgyzdaamy.adapter.category

import io.flaterlab.kyrgyzdaamy.service.response.Category

interface CategoryListener {
    fun onCategoryClick(item: Category)
}