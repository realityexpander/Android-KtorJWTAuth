package com.realityexpander.androidktorjwtauth.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.realityexpander.androidktorjwtauth.auth.AuthResult
import com.realityexpander.androidktorjwtauth.ui.authroutes.AuthenticatedUiEvent
import com.realityexpander.androidktorjwtauth.ui.authroutes.AuthenticatedViewModel
import com.realityexpander.androidktorjwtauth.ui.destinations.AuthScreenDestination
import com.realityexpander.androidktorjwtauth.ui.destinations.AuthenticatedScreenDestination

@Composable
@Destination
fun AuthenticatedScreen(
    navigator: DestinationsNavigator, // must be first parameter
    viewModel: AuthenticatedViewModel = hiltViewModel(),
    signInUsername : String,
) {
    val context = LocalContext.current
    val state = viewModel.state

    // Respond to responses from the view model
    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect { result ->
            when (result) {
                is Unit -> {
                    Toast.makeText(context, "Logging out. ", Toast.LENGTH_LONG).show()

                    navigator.navigate(AuthScreenDestination) {
                        popUpTo(AuthenticatedScreenDestination.route) {
                            inclusive = true // disables back button going to the previous screen
                        }
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "You're authenticated as ${state.signInUsername}")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.onEvent(AuthenticatedUiEvent.Logout)
            },
        ) {
            Text(text = "Logout",
                color = MaterialTheme.colors.onPrimary)
        }
    }
}