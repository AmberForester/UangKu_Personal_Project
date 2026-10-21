package com.example.uangku.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = darkSkyBlue,
    onPrimary = darkBgSlate,
    primaryContainer = darkSkyBlueContainer,
    onPrimaryContainer = Color(0xFFBAE6FD),

    background = darkBgSlate,
    onBackground = darkTextPrimary,

    surface = darkSurfaceSlate,
    onSurface = darkTextPrimary,
    surfaceVariant = darkBorderSlate,
    onSurfaceVariant = darkTextSecondary,

    outline = darkBorderSlate,
    outlineVariant = darkSurfaceSlate
)

private val LightColorScheme = lightColorScheme(
    primary = mainSkyBlue,
    onPrimary = Color.White,
    primaryContainer = secondaryBgSkyBlue,
    onPrimaryContainer = mainSkyBlue,

    background = mainBgSkyBlue,
    onBackground = Color(0xFF0F172A),

    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF64748B),

    outline = secondaryBgSkyBlue,
    outlineVariant = Color(0xFFF1F5F9)
)

@Composable
fun UangKuTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Set false agar warna tema kustom konsisten
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

