package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

@Composable
fun MyApplicationTheme(
    palette: AppThemePalette = AppThemePalette.WHITE_BLUE,
    content: @Composable () -> Unit
) {
    val colors = getAppColors(palette)

    val colorScheme = if (colors.isDark) {
        darkColorScheme(
            primary = colors.primary,
            onPrimary = Color.White,
            primaryContainer = colors.container,
            onPrimaryContainer = colors.textPrimary,
            secondary = colors.secondary,
            onSecondary = Color.White,
            secondaryContainer = colors.chipBg,
            onSecondaryContainer = colors.textBody,
            tertiary = colors.accent,
            background = colors.bg,
            onBackground = colors.textPrimary,
            surface = colors.surface,
            onSurface = colors.textPrimary,
            surfaceVariant = colors.container,
            onSurfaceVariant = colors.textBody,
            outline = colors.cardBorder,
            outlineVariant = colors.borderSubtle
        )
    } else {
        lightColorScheme(
            primary = colors.primary,
            onPrimary = Color.White,
            primaryContainer = colors.container,
            onPrimaryContainer = colors.textPrimary,
            secondary = colors.secondary,
            onSecondary = Color.White,
            secondaryContainer = colors.chipBg,
            onSecondaryContainer = colors.textBody,
            tertiary = colors.accent,
            background = colors.bg,
            onBackground = colors.textPrimary,
            surface = colors.surface,
            onSurface = colors.textPrimary,
            surfaceVariant = colors.container,
            onSurfaceVariant = colors.textBody,
            outline = colors.cardBorder,
            outlineVariant = colors.borderSubtle
        )
    }

    CompositionLocalProvider(LocalAppColors provides colors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
