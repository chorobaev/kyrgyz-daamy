package com.timelysoft.kainarapp.service.model2.response2

data class AccessToken(
    val accessToken : String,
    val userName : String,
    val roles : List<String>
)