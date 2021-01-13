package com.timelysoft.kainarapp.adapter.news

import com.timelysoft.kainarapp.service.model2.response2.NewsResponse

interface NewsListener {
    fun onDiscountCLick(item: NewsResponse)
}