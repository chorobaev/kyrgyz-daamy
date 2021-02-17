package com.timelysoft.amore.service.response

data class OnlinePaymentResponse(
    val amount: Int,
    val clientId: String,
    val currency: String,
    val failUrl: String,
    val hash: String,
    val instalment: Int,
    val language: String,
    val okUrl: String,
    val paymentId: String,
    val postUrl: String,
    val refreshTime: String,
    val relativeFailUrl: String,
    val relativeOkUrl: String,
    val rnd: String,
    val storeType: String,
    val transactionType: String
)