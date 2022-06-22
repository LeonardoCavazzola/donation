package com.example.donation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RedDarkColorPalette = darkColors(
    primary = Red900,
    primaryVariant = Red900Dark,
    secondary = Red900,
    secondaryVariant = Red900Dark,
    error = Red500,
)

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val RedLightColorPalette = lightColors(
    primary = Red900,
    primaryVariant = Red900Dark,
    secondary = Red900,
    secondaryVariant = Red900Dark,
    error = Red500,
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200
)

@Composable
fun AppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        RedDarkColorPalette
    } else {
        RedLightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
