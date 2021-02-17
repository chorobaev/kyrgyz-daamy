package com.timelysoft.amore.adapter.news

import com.timelysoft.amore.service.response.NewsResponse

interface NewsListener {
    fun onDiscountCLick(item: NewsResponse)
}