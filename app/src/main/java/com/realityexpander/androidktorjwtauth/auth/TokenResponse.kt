package com.realityexpander.androidktorjwtauth.auth

data class TokenResponse(
    val token: String,
    val username: String = ""
)
