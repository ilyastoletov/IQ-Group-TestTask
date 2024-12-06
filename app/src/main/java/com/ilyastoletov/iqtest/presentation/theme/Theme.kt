package com.ilyastoletov.iqtest.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF282828),
    secondary = Color(0xFFFF9025),
    tertiary = Pink40,
    outlineVariant = Color(0xFFC4C7C8),
    surface = Color(0xFFF4F4F4),
    onSurface = Color(0xFF1D1B20),
    onSurfaceVariant = Color(0xFF444748)
)

@Composable
fun IQGroupTestTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}