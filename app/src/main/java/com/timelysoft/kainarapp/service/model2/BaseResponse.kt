package com.timelysoft.kainarapp.service.model2

import com.google.gson.annotations.SerializedName

class BaseResponse<T> (
        @SerializedName("status")val status: Int,
        @SerializedName("errors")val errors: HashMap<String, List<String>>,
        @SerializedName("data")val data : T
)