package io.flaterlab.kyrgyzdaamy.service.response

data class AccessToken(
    val accessToken : String,
    val userName : String,
    val roles : List<String>
)