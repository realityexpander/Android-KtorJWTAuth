package com.realityexpander.androidktorjwtauth.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.realityexpander.androidktorjwtauth.auth.AuthResult
import com.realityexpander.androidktorjwtauth.ui.destinations.AuthScreenDestination
import com.realityexpander.androidktorjwtauth.ui.destinations.AuthenticatedScreenDestination
import kotlinx.coroutines.flow.collect

@Composable
@Destination(start = true)
fun AuthScreen(
    navigator: DestinationsNavigator, // must be first parameter
    viewModel: MainViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect { result ->
            when(result) {
                is AuthResult.Success -> {
                    Toast.makeText(context, "Success. " + result.data, Toast.LENGTH_LONG).show()
                }
                is AuthResult.Authorized -> {
                    navigator.navigate(AuthenticatedScreenDestination) {
                        popUpTo(AuthScreenDestination.route) {
                            inclusive = true // disables back button going to the previous screen
                        }
                    }
                }
                is AuthResult.Unauthorized -> {
                    Toast.makeText(context,
                        "Unauthorized. " + (result.data?.toString() ?: ""), Toast.LENGTH_LONG).show()
                }
                is AuthResult.Error -> {
                    Toast.makeText(context, result.errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = state.signUpUsername,
            onValueChange = {
                viewModel.onEvent(AuthUiEvent.SignUpUsernameChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Username")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = state.signUpPassword,
            onValueChange = {
                viewModel.onEvent(AuthUiEvent.SignUpPasswordChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Password")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.onEvent(AuthUiEvent.SignUp)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Sign up")
        }

        Spacer(modifier = Modifier.height(64.dp))

        TextField(
            value = state.signInUsername,
            onValueChange = {
                viewModel.onEvent(AuthUiEvent.SignInUsernameChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Username")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = state.signInPassword,
            onValueChange = {
                viewModel.onEvent(AuthUiEvent.SignInPasswordChanged(it))
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Password")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.onEvent(AuthUiEvent.SignIn)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Sign in")
        }
    }
    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}