package com.example.cardapiodigital.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = darkColorScheme(
    primary = AppGold,
    onPrimary = AppBlack,
    primaryContainer = AppGoldSoft,
    onPrimaryContainer = AppBlack,
    secondary = AppGold,
    onSecondary = AppBlack,
    tertiary = AppGoldSoft,
    onTertiary = AppBlack,
    background = AppBlack,
    onBackground = AppWhite,
    surface = AppSurface,
    onSurface = AppWhite,
    surfaceVariant = AppSurfaceVariant,
    onSurfaceVariant = AppWhite,
    outline = AppGoldSoft,
    outlineVariant = AppSurfaceVariant
)

@Composable
fun CardapioDigitalTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = Typography,
        content = content
    )
}
