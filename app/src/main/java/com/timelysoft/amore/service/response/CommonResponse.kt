package com.timelysoft.amore.service.response

import com.google.gson.annotations.SerializedName


class CommonResponse<T>(
    @SerializedName("array",alternate = ["order","order_id","value"])
    val data: T,
    val result: Boolean,
    val msg: String
)