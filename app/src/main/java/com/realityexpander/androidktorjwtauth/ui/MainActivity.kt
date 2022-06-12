package com.realityexpander.androidktorjwtauth.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ramcosta.composedestinations.DestinationsNavHost
import com.realityexpander.androidktorjwtauth.ui.theme.AndroidKtorJWTAuthTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidKtorJWTAuthTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}