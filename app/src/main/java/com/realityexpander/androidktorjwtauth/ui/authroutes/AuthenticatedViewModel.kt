package com.realityexpander.androidktorjwtauth.ui.authroutes

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realityexpander.androidktorjwtauth.auth.AuthRepository
import com.realityexpander.androidktorjwtauth.ui.login.AuthState
import com.realityexpander.androidktorjwtauth.ui.login.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthenticatedViewModel @Inject constructor(
    private val repo: AuthRepository, // uses interface
    private val prefs: SharedPreferences
): ViewModel() {

    var state by mutableStateOf(AuthState())

    private val resultChannel = Channel<Unit>()
    val authResults = resultChannel.receiveAsFlow()

    init {
    }

    fun onEvent(event: AuthenticatedUiEvent) {
        when(event) {
            is AuthenticatedUiEvent.Logout -> {
                logout()
            }
            else -> {
                // ignore
            }
        }
    }


    private fun logout() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            // Remove the token, prep for logout
            prefs.edit()
                .remove("jwt")
                .apply()

            resultChannel.send(Unit)

            state = state.copy(isLoading = false)
        }
    }


}