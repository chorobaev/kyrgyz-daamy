package com.timelysoft.kainarapp.service.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AuthResponse {
    @SerializedName("access_token")
    @Expose
    var accessToken: String = ""
    @SerializedName("refresh_token")
    @Expose
    var refreshToken: String = ""

}
