package com.timelysoft.amore.adapter.news

import com.timelysoft.amore.service.model2.response2.NewsResponse

interface NewsListener {
    fun onDiscountCLick(item: NewsResponse)
}