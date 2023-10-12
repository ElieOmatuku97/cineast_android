package com.example.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable


private val LightColors = lightColorScheme(
    primary = elieomatuku.cineast_android.md_theme_light_primary,
    onPrimary = elieomatuku.cineast_android.md_theme_light_onPrimary,
    primaryContainer = elieomatuku.cineast_android.md_theme_light_primaryContainer,
    onPrimaryContainer = elieomatuku.cineast_android.md_theme_light_onPrimaryContainer,
    secondary = elieomatuku.cineast_android.md_theme_light_secondary,
    onSecondary = elieomatuku.cineast_android.md_theme_light_onSecondary,
    secondaryContainer = elieomatuku.cineast_android.md_theme_light_secondaryContainer,
    onSecondaryContainer = elieomatuku.cineast_android.md_theme_light_onSecondaryContainer,
    tertiary = elieomatuku.cineast_android.md_theme_light_tertiary,
    onTertiary = elieomatuku.cineast_android.md_theme_light_onTertiary,
    tertiaryContainer = elieomatuku.cineast_android.md_theme_light_tertiaryContainer,
    onTertiaryContainer = elieomatuku.cineast_android.md_theme_light_onTertiaryContainer,
    error = elieomatuku.cineast_android.md_theme_light_error,
    errorContainer = elieomatuku.cineast_android.md_theme_light_errorContainer,
    onError = elieomatuku.cineast_android.md_theme_light_onError,
    onErrorContainer = elieomatuku.cineast_android.md_theme_light_onErrorContainer,
    background = elieomatuku.cineast_android.md_theme_light_background,
    onBackground = elieomatuku.cineast_android.md_theme_light_onBackground,
    surface = elieomatuku.cineast_android.md_theme_light_surface,
    onSurface = elieomatuku.cineast_android.md_theme_light_onSurface,
    surfaceVariant = elieomatuku.cineast_android.md_theme_light_surfaceVariant,
    onSurfaceVariant = elieomatuku.cineast_android.md_theme_light_onSurfaceVariant,
    outline = elieomatuku.cineast_android.md_theme_light_outline,
    inverseOnSurface = elieomatuku.cineast_android.md_theme_light_inverseOnSurface,
    inverseSurface = elieomatuku.cineast_android.md_theme_light_inverseSurface,
    inversePrimary = elieomatuku.cineast_android.md_theme_light_inversePrimary,
    surfaceTint = elieomatuku.cineast_android.md_theme_light_surfaceTint,
    outlineVariant = elieomatuku.cineast_android.md_theme_light_outlineVariant,
    scrim = elieomatuku.cineast_android.md_theme_light_scrim,
)


private val DarkColors = darkColorScheme(
    primary = elieomatuku.cineast_android.md_theme_dark_primary,
    onPrimary = elieomatuku.cineast_android.md_theme_dark_onPrimary,
    primaryContainer = elieomatuku.cineast_android.md_theme_dark_primaryContainer,
    onPrimaryContainer = elieomatuku.cineast_android.md_theme_dark_onPrimaryContainer,
    secondary = elieomatuku.cineast_android.md_theme_dark_secondary,
    onSecondary = elieomatuku.cineast_android.md_theme_dark_onSecondary,
    secondaryContainer = elieomatuku.cineast_android.md_theme_dark_secondaryContainer,
    onSecondaryContainer = elieomatuku.cineast_android.md_theme_dark_onSecondaryContainer,
    tertiary = elieomatuku.cineast_android.md_theme_dark_tertiary,
    onTertiary = elieomatuku.cineast_android.md_theme_dark_onTertiary,
    tertiaryContainer = elieomatuku.cineast_android.md_theme_dark_tertiaryContainer,
    onTertiaryContainer = elieomatuku.cineast_android.md_theme_dark_onTertiaryContainer,
    error = elieomatuku.cineast_android.md_theme_dark_error,
    errorContainer = elieomatuku.cineast_android.md_theme_dark_errorContainer,
    onError = elieomatuku.cineast_android.md_theme_dark_onError,
    onErrorContainer = elieomatuku.cineast_android.md_theme_dark_onErrorContainer,
    background = elieomatuku.cineast_android.md_theme_dark_background,
    onBackground = elieomatuku.cineast_android.md_theme_dark_onBackground,
    surface = elieomatuku.cineast_android.md_theme_dark_surface,
    onSurface = elieomatuku.cineast_android.md_theme_dark_onSurface,
    surfaceVariant = elieomatuku.cineast_android.md_theme_dark_surfaceVariant,
    onSurfaceVariant = elieomatuku.cineast_android.md_theme_dark_onSurfaceVariant,
    outline = elieomatuku.cineast_android.md_theme_dark_outline,
    inverseOnSurface = elieomatuku.cineast_android.md_theme_dark_inverseOnSurface,
    inverseSurface = elieomatuku.cineast_android.md_theme_dark_inverseSurface,
    inversePrimary = elieomatuku.cineast_android.md_theme_dark_inversePrimary,
    surfaceTint = elieomatuku.cineast_android.md_theme_dark_surfaceTint,
    outlineVariant = elieomatuku.cineast_android.md_theme_dark_outlineVariant,
    scrim = elieomatuku.cineast_android.md_theme_dark_scrim,
)

@Composable
fun AppTheme(
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable() () -> Unit
) {
  val colors = if (!useDarkTheme) {
    LightColors
  } else {
    DarkColors
  }

  MaterialTheme(
    colorScheme = colors,
    content = content
  )
}