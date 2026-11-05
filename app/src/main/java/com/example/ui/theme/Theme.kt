package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val GsmColorScheme = darkColorScheme(
    primary = GsmCyan,
    onPrimary = GsmBackground,
    primaryContainer = GsmSurfaceVariant,
    onPrimaryContainer = GsmCyan,
    secondary = GsmGold,
    onSecondary = GsmBackground,
    secondaryContainer = GsmSurfaceVariant,
    onSecondaryContainer = GsmGold,
    tertiary = GsmGreen,
    onTertiary = GsmBackground,
    background = GsmBackground,
    onBackground = TextPrimary,
    surface = GsmSurface,
    onSurface = TextPrimary,
    surfaceVariant = GsmSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = GsmBorder,
    error = GsmRed
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = GsmColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = GsmBackground.toArgb()
            window.navigationBarColor = GsmBackground.toArgb()
            val controller = WindowCompat.getInsetsController(window, view)
            controller.isAppearanceLightStatusBars = false
            controller.isAppearanceLightNavigationBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
