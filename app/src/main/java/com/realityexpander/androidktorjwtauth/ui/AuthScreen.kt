package com.realityexpander.androidktorjwtauth.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.realityexpander.androidktorjwtauth.auth.AuthResult
import com.realityexpander.androidktorjwtauth.ui.destinations.AuthScreenDestination
import com.realityexpander.androidktorjwtauth.ui.destinations.AuthenticatedScreenDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
@Destination(start = true)
fun AuthScreen(
    navigator: DestinationsNavigator, // must be first parameter
    viewModel: MainViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val context = LocalContext.current

    val focusManager = LocalFocusManager.current

    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    val bringIntoViewRequesters = mutableListOf<BringIntoViewRequester>()
    repeat(4) {
        bringIntoViewRequesters += remember { BringIntoViewRequester() }
    }
    val buttonViewRequester = remember { BringIntoViewRequester() }

    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect { result ->
            when (result) {
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
                    Toast.makeText(
                        context,
                        "Unauthorized. " + (result.data?.toString() ?: ""), Toast.LENGTH_LONG
                    ).show()
                }
                is AuthResult.Error -> {
                    Toast.makeText(context, result.errorMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun requestBringItemIntoView(focusState: FocusState, viewItem: Int) {
        if (focusState.isFocused) {
            coroutineScope.launch {
                delay(300)
                if (viewItem >= 2) {
                    buttonViewRequester.bringIntoView()
                } else {
                    bringIntoViewRequesters[viewItem].bringIntoView()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .bringIntoViewRequester(bringIntoViewRequesters[0]),
        ) {
            TextField(
                value = state.signUpUsername,
                onValueChange = {
                    viewModel.onEvent(AuthUiEvent.SignUpUsernameChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusEvent { focusState ->
                        requestBringItemIntoView(focusState, 0)
                    },
                placeholder = {
                    Text(
                        text = "Username",
                        style = MaterialTheme.typography.body1
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .bringIntoViewRequester(bringIntoViewRequesters[1]),
        ) {
            TextField(
                value = state.signUpPassword,
                onValueChange = {
                    viewModel.onEvent(AuthUiEvent.SignUpPasswordChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusEvent { focusState ->
                        requestBringItemIntoView(focusState, 1)
                    },
                placeholder = {
                    Text(text = "Password",
                        style = MaterialTheme.typography.body1
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.onEvent(AuthUiEvent.SignUp)
            },
        ) {
            Text(text = "Sign up")
        }
        Spacer(modifier = Modifier.height(32.dp))


        Row(
            modifier = Modifier
                .bringIntoViewRequester(bringIntoViewRequesters[2]),
        ) {
            TextField(
                value = state.signInUsername,
                onValueChange = {
                    viewModel.onEvent(AuthUiEvent.SignInUsernameChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusEvent { focusState ->
                        requestBringItemIntoView(focusState, 2)
                    },
                placeholder = {
                    Text(text = "Username",
                        style = MaterialTheme.typography.body1
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .bringIntoViewRequester(bringIntoViewRequesters[3]),
        ) {
            TextField(
                value = state.signInPassword,
                onValueChange = {
                    viewModel.onEvent(AuthUiEvent.SignInPasswordChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusEvent { focusState ->
                        requestBringItemIntoView(focusState, 3)
                    },
                placeholder = {
                    Text(text = "Password",
                        style = MaterialTheme.typography.body1
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.onEvent(AuthUiEvent.SignIn)
            },
            modifier = Modifier
                .bringIntoViewRequester(buttonViewRequester),
        ) {
            Text(text = "Sign in")

        }
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
