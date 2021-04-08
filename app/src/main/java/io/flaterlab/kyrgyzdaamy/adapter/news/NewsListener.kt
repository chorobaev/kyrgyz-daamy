package io.flaterlab.kyrgyzdaamy.adapter.news

import io.flaterlab.kyrgyzdaamy.service.response.NewsResponse

interface NewsListener {
    fun onDiscountCLick(item: NewsResponse)
}