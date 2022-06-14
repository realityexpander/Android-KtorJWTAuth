package com.realityexpander.androidktorjwtauth.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

private val DarkColorPalette = darkColors(
    primary = ColorPrimary,
    background = DarkGray,
    primaryVariant = Purple700,
    secondary = Teal200,
    onBackground = DarkGray,
    onPrimary = DarkGray
)

//primary: Color = Color(0xFF6200EE),
//primaryVariant: Color = Color(0xFF3700B3),
//secondary: Color = Color(0xFF03DAC6),
//secondaryVariant: Color = Color(0xFF018786),
//background: Color = Color.White,
//surface: Color = Color.White,
//error: Color = Color(0xFFB00020),
//onPrimary: Color = Color.White,
//onSecondary: Color = Color.Black,
//onBackground: Color = Color.Black,
//onSurface: Color = Color.Black,
//onError: Color = Color.White

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun AndroidKtorJWTAuthTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = {
            if (darkTheme) {
                ProvideTextStyle(
                    value = TextStyle(color = TextWhite),
                    content = content
                )
            } else {
                content()
            }
        }
    )
}