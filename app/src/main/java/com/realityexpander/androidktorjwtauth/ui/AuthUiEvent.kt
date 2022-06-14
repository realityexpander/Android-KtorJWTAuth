package com.realityexpander.androidktorjwtauth.ui

sealed class AuthUiEvent {
    data class SignUpUsernameChanged(val value: String): AuthUiEvent()
    data class SignUpEmailChanged(val value: String): AuthUiEvent()
    data class SignUpPasswordChanged(val value: String): AuthUiEvent()
    object SignUp: AuthUiEvent()

    data class SignInUsernameChanged(val value: String): AuthUiEvent()
    data class SignInEmailChanged(val value: String): AuthUiEvent()
    data class SignInPasswordChanged(val value: String): AuthUiEvent()
    object SignIn: AuthUiEvent()
}


//const val SIGNUP_USERNAME = 1
//const val SIGNUP_PASSWORD = 2
//const val SIGNUP_EMAIL = 3
//const val SIGNIN_USERNAME = 4
//const val SIGNIN_PASSWORD = 5
//const val SIGNIN_EMAIL = 6