package com.realityexpander.androidktorjwtauth.ui.authroutes

sealed class AuthenticatedUiEvent {
    object Logout: AuthenticatedUiEvent()
}