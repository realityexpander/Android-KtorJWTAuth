package com.realityexpander.androidktorjwtauth.ui.login

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
