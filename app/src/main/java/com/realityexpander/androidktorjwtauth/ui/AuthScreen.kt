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
import com.realityexpander.androidktorjwtauth.ui.theme.TextWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class ViewIndex(val index: Int) {
    SIGNUP_USERNAME(0),
    SIGNUP_EMAIL(1),
    SIGNUP_PASSWORD(2),
    SIGNIN_USERNAME(3),
    SIGNIN_EMAIL(4),
    SIGNIN_PASSWORD(5),
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
@Destination(start = true)
fun AuthScreen(
    navigator: DestinationsNavigator, // must be first parameter
    viewModel: MainViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Setup views to scroll to upon focus
    val bringIntoViewRequesters = mutableListOf<BringIntoViewRequester>()
    repeat(ViewIndex.values().size) {
        bringIntoViewRequesters += remember { BringIntoViewRequester() }
    }
    val signInButtonViewRequester = remember { BringIntoViewRequester() }

    // Respond to responses from the view model
    LaunchedEffect(viewModel, context) {
        viewModel.authResults.collect { result ->
            when (result) {
                is AuthResult.Success -> {
                    Toast.makeText(context, "Success. " + result.data, Toast.LENGTH_LONG).show()
                }
                is AuthResult.Authorized -> {
                    Toast.makeText(context, "Authorized. " + result.data, Toast.LENGTH_LONG).show()

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
                if (viewItem >= ViewIndex.SIGNIN_USERNAME.index) {
                    signInButtonViewRequester.bringIntoView()
                } else {
                    bringIntoViewRequesters[viewItem].bringIntoView()
                }
            }
        }
    }

    @Composable
    @OptIn(ExperimentalFoundationApi::class)
    fun TextEntryItem(
        viewIndex: ViewIndex,
        value: String,
        placeholderText: String,
        onValueChange: (String) -> Unit) {
        Row(
            modifier = Modifier
                .bringIntoViewRequester(bringIntoViewRequesters[viewIndex.index]),
        ) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusEvent { focusState ->
                        requestBringItemIntoView(focusState, viewIndex.index)
                    },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                placeholder = {
                    Text(
                        text = placeholderText,
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.primary
                    )
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
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
        TextEntryItem(
            viewIndex = ViewIndex.SIGNUP_USERNAME,
            value = state.signUpUsername,
            placeholderText = "Username",
            onValueChange = { newValue -> viewModel.onEvent(AuthUiEvent.SignUpUsernameChanged(newValue)) }
        )
        TextEntryItem(
            viewIndex = ViewIndex.SIGNUP_EMAIL,
            value = state.signUpEmail,
            placeholderText = "Email",
            onValueChange = { newValue -> viewModel.onEvent(AuthUiEvent.SignUpEmailChanged(newValue)) }
        )
        TextEntryItem(
            viewIndex = ViewIndex.SIGNUP_PASSWORD,
            value = state.signUpPassword,
            placeholderText = "Password",
            onValueChange = { newValue -> viewModel.onEvent(AuthUiEvent.SignUpPasswordChanged(newValue)) }
        )
        Button(
            onClick = {
                viewModel.onEvent(AuthUiEvent.SignUp)
            },
        ) {
            Text(text = "Sign up"
                , color = MaterialTheme.colors.onPrimary)
        }
        Spacer(modifier = Modifier.height(32.dp))


        TextEntryItem(
            viewIndex = ViewIndex.SIGNIN_USERNAME,
            value = state.signInUsername,
            placeholderText = "Username",
            onValueChange = { newValue -> viewModel.onEvent(AuthUiEvent.SignInUsernameChanged(newValue)) }
        )
        TextEntryItem(
            viewIndex = ViewIndex.SIGNIN_EMAIL,
            value = state.signInEmail,
            placeholderText = "Email",
            onValueChange = { newValue -> viewModel.onEvent(AuthUiEvent.SignInEmailChanged(newValue)) }
        )
        TextEntryItem(
            viewIndex = ViewIndex.SIGNIN_PASSWORD,
            value = state.signInPassword,
            placeholderText = "Password",
            onValueChange = { newValue -> viewModel.onEvent(AuthUiEvent.SignInPasswordChanged(newValue)) }
        )
        Button(
            onClick = {
                viewModel.onEvent(AuthUiEvent.SignIn)
            },
            modifier = Modifier
                .bringIntoViewRequester(signInButtonViewRequester),
        ) {
            Text(text = "Sign in",
                color = MaterialTheme.colors.onPrimary)

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
