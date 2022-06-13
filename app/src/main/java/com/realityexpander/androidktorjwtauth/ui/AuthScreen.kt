package com.realityexpander.androidktorjwtauth.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
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


    val bringIntoViewRequesters = mutableListOf(remember { BringIntoViewRequester() })
    val buttonViewRequester = remember { BringIntoViewRequester() }
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current

    repeat(13) {
        bringIntoViewRequesters += remember { BringIntoViewRequester() }
    }

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


//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState()),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        repeat(6) { PlaceholderCard() }
//
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//                .bringIntoViewRequester(bringIntoViewRequester),
//            elevation = 8.dp
//        ) {
//            Column {
//                Text(modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp), text = "Please fill something in")
//                TextField(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp)
//                        .onFocusEvent { focusState ->
//                            if (focusState.isFocused) {
//                                coroutineScope.launch {
//                                    bringIntoViewRequester.bringIntoView()
//                                }
//                            }
//                        },
//                    placeholder = { Text(text = "Some input") },
//                    value = state.signInPassword,
//                    singleLine = true,
//                    onValueChange = { viewModel.onEvent(AuthUiEvent.SignInPasswordChanged(it)) },
//                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
//                )
//                Button(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(start = 16.dp, bottom = 16.dp, end = 16.dp),
//                    onClick = { /*Nothing*/ }) {
//                    Text("Click")
//                }
//            }
//        }
//
//        repeat(10) { PlaceholderCard() }
//    }

    fun requestBringIntoView(focusState: FocusState, viewItem: Int) {
        if (focusState.isFocused) {
            coroutineScope.launch {
                delay(200)
                if (viewItem >= 2) {
                    buttonViewRequester.bringIntoView()
                } else {
                    bringIntoViewRequesters[viewItem].bringIntoView()
                }
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())

    ) {

        repeat(12) { i ->
            Row(
                modifier = Modifier
                    .bringIntoViewRequester(bringIntoViewRequesters[i]),
            ) {
                TextField(
                    value = "",
                    onValueChange = {},
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                    modifier = Modifier
                        .onFocusEvent { focusState ->
                            requestBringIntoView(focusState, i)
                        },
                )
            }
        }


        Button(
            onClick = {},
            modifier = Modifier
                .bringIntoViewRequester(buttonViewRequester)
        ) {
            Text(text = "Yeah Visible")
        }
    }


//    Column(
//        modifier = Modifier.fillMaxSize(),
//    ) {
//        LazyColumn( // Workaround to ensure softkeyboard will scroll up the view
//            Modifier.weight(.5f),
//            verticalArrangement = Arrangement.Top,
//        ) {}


    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
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
                        requestBringIntoView(focusState, 0)
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
                        requestBringIntoView(focusState, 1)
                    },
                placeholder = {
                    Text(text = "Password")
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
                        requestBringIntoView(focusState, 2)
                    },
                placeholder = {
                    Text(text = "Username")
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
                        requestBringIntoView(focusState, 3)
                    },
                placeholder = {
                    Text(text = "Password")
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

//@Composable
//private fun PlaceholderCard() {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp),
//        elevation = 8.dp
//    ) {
//        Text(modifier = Modifier.padding(32.dp), text = "Placeholder")
//    }
//}