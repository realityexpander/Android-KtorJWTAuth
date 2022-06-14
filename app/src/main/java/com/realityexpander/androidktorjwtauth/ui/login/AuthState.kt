package com.realityexpander.androidktorjwtauth.ui.login

data class AuthState(
    val isLoading: Boolean = false,
    val signUpUsername: String = "",
    val signUpEmail: String = "",
    val signUpPassword: String = "",
    val signInUsername: String = "",
    val signInEmail: String = "",
    val signInPassword: String = ""
)
