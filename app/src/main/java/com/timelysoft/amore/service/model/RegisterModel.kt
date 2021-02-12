package com.timelysoft.amore.service.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterModel (
    @SerializedName("PhoneNumber")
    @Expose
    var phoneNumber: String = "",

    @SerializedName("FirstName")
    @Expose
    var firstName: String = "",

    @SerializedName("LastName")
    @Expose
    var lastName: String = "",

    @SerializedName("Birthday")
    @Expose
    var birthday: String? = null,

    @SerializedName("Sex")
    @Expose
    var sex: Int? = null
)