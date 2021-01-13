package com.timelysoft.kainarapp.service.response

class OrderOnlinePayResponse(
    val amount: Int,
    val clientId: String,
    val currency: String,
    val failUrl: String,
    val hash: String,
    val islemtipi: String,
    val lang: String,
    val msg: Any,
    val oid: String,
    val okUrl: String,
    val refreshtime: String,
    val result: Boolean,
    val rnd: String,
    val storetype: String,
    val taksit: String
)