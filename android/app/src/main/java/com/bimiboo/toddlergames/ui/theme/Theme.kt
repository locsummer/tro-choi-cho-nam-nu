package com.bimiboo.toddlergames.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PastelBlue,
    secondary = PastelOrange,
    tertiary = PastelGreen,
    background = LightBackground,
    surface = CardBackground
)

@Composable
fun ToddlerGamesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
