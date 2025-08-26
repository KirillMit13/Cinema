package com.example.cinema

import android.app.Application
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.cinema.ui.components.NeonColors

class SkillCinemaApp : Application()

private val DarkColorScheme = darkColorScheme(
    primary = NeonColors.Primary,
    onPrimary = Color.Black,
    primaryContainer = NeonColors.Primary.copy(alpha = 0.8f),
    onPrimaryContainer = Color.Black,
    secondary = NeonColors.Secondary,
    onSecondary = Color.White,
    secondaryContainer = NeonColors.Secondary.copy(alpha = 0.8f),
    onSecondaryContainer = Color.White,
    tertiary = NeonColors.Tertiary,
    onTertiary = Color.White,
    tertiaryContainer = NeonColors.Tertiary.copy(alpha = 0.8f),
    onTertiaryContainer = Color.White,
    error = NeonColors.GlowPink,
    onError = Color.White,
    errorContainer = NeonColors.GlowPink.copy(alpha = 0.8f),
    onErrorContainer = Color.White,
    background = NeonColors.Background,
    onBackground = NeonColors.TextPrimary,
    surface = NeonColors.Surface,
    onSurface = NeonColors.TextPrimary,
    surfaceVariant = NeonColors.SurfaceVariant,
    onSurfaceVariant = NeonColors.TextSecondary,
    outline = Color(0xFF404040),
    outlineVariant = Color(0xFF606060),
    scrim = Color.Black,
    inverseSurface = NeonColors.TextPrimary,
    inverseOnSurface = NeonColors.Background,
    inversePrimary = NeonColors.Primary,
    surfaceTint = NeonColors.Primary,
    surfaceBright = Color(0xFF3A3A3A),
    surfaceDim = NeonColors.Surface,
    surfaceContainer = NeonColors.SurfaceVariant,
    surfaceContainerHigh = Color(0xFF3A3A3A),
    surfaceContainerHighest = Color(0xFF4A4A4A),
    surfaceContainerLow = NeonColors.Surface,
    surfaceContainerLowest = NeonColors.Background
)

private val LightColorScheme = lightColorScheme(
    primary = NeonColors.Primary,
    onPrimary = Color.Black,
    primaryContainer = NeonColors.Primary.copy(alpha = 0.8f),
    onPrimaryContainer = Color.Black,
    secondary = NeonColors.Secondary,
    onSecondary = Color.White,
    secondaryContainer = NeonColors.Secondary.copy(alpha = 0.8f),
    onSecondaryContainer = Color.White,
    tertiary = NeonColors.Tertiary,
    onTertiary = Color.White,
    tertiaryContainer = NeonColors.Tertiary.copy(alpha = 0.8f),
    onTertiaryContainer = Color.White,
    error = NeonColors.GlowPink,
    onError = Color.White,
    errorContainer = NeonColors.GlowPink.copy(alpha = 0.8f),
    onErrorContainer = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = Color(0xFF666666),
    outline = Color(0xFFCCCCCC),
    outlineVariant = Color(0xFFCCCCCC),
    scrim = Color.Black,
    inverseSurface = Color.Black,
    inverseOnSurface = Color.White,
    inversePrimary = NeonColors.Primary,
    surfaceTint = NeonColors.Primary,
    surfaceBright = Color.White,
    surfaceDim = Color(0xFFF5F5F5),
    surfaceContainer = Color.White,
    surfaceContainerHigh = Color(0xFFF0F0F0),
    surfaceContainerHighest = Color(0xFFE5E5E5),
    surfaceContainerLow = Color.White,
    surfaceContainerLowest = Color.White
)

@Composable
fun SkillCinemaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}