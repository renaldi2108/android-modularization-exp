package com.example.core.shared.designsystem.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val md_primary            = Color(0xFF4F46E5)
private val md_onPrimary          = Color(0xFFFFFFFF)
private val md_primaryContainer   = Color(0xFFE0E0FF)
private val md_onPrimaryContainer = Color(0xFF120065)
private val md_secondary          = Color(0xFF5A5D72)
private val md_onSecondary        = Color(0xFFFFFFFF)
private val md_secondaryContainer = Color(0xFFDFE1F9)
private val md_onSecondaryContainer = Color(0xFF171B2C)
private val md_tertiary           = Color(0xFF00687A)
private val md_onTertiary         = Color(0xFFFFFFFF)
private val md_tertiaryContainer  = Color(0xFFAAEDFF)
private val md_onTertiaryContainer = Color(0xFF001F26)
private val md_error              = Color(0xFFBA1A1A)
private val md_onError            = Color(0xFFFFFFFF)
private val md_errorContainer     = Color(0xFFFFDAD6)
private val md_onErrorContainer   = Color(0xFF410002)
private val md_background         = Color(0xFFFEFBFF)
private val md_onBackground       = Color(0xFF1B1B1F)
private val md_surface            = Color(0xFFFEFBFF)
private val md_onSurface          = Color(0xFF1B1B1F)
private val md_surfaceVariant     = Color(0xFFE3E1EC)
private val md_onSurfaceVariant   = Color(0xFF46464F)
private val md_outline            = Color(0xFF777680)
private val md_outlineVariant     = Color(0xFFC7C5D0)
private val md_inverseSurface     = Color(0xFF303034)
private val md_inverseOnSurface   = Color(0xFFF3F0F4)
private val md_inversePrimary     = Color(0xFFC0C1FF)

private val dk_primary            = Color(0xFFC0C1FF)
private val dk_onPrimary          = Color(0xFF1A1BA8)
private val dk_primaryContainer   = Color(0xFF3730CC)
private val dk_onPrimaryContainer = Color(0xFFE0E0FF)
private val dk_secondary          = Color(0xFFC3C5DD)
private val dk_onSecondary        = Color(0xFF2C2F42)
private val dk_secondaryContainer = Color(0xFF424659)
private val dk_onSecondaryContainer = Color(0xFFDFE1F9)
private val dk_tertiary           = Color(0xFF82D3E8)
private val dk_onTertiary         = Color(0xFF00363F)
private val dk_tertiaryContainer  = Color(0xFF004E5B)
private val dk_onTertiaryContainer = Color(0xFFAAEDFF)
private val dk_error              = Color(0xFFFFB4AB)
private val dk_onError            = Color(0xFF690005)
private val dk_errorContainer     = Color(0xFF93000A)
private val dk_onErrorContainer   = Color(0xFFFFDAD6)
private val dk_background         = Color(0xFF1B1B1F)
private val dk_onBackground       = Color(0xFFE4E1E6)
private val dk_surface            = Color(0xFF1B1B1F)
private val dk_onSurface          = Color(0xFFE4E1E6)
private val dk_surfaceVariant     = Color(0xFF46464F)
private val dk_onSurfaceVariant   = Color(0xFFC7C5D0)
private val dk_outline            = Color(0xFF918F9A)
private val dk_outlineVariant     = Color(0xFF46464F)
private val dk_inverseSurface     = Color(0xFFE4E1E6)
private val dk_inverseOnSurface   = Color(0xFF303034)
private val dk_inversePrimary     = Color(0xFF4F46E5)

internal val LightColors = lightColorScheme(
    primary = md_primary, onPrimary = md_onPrimary,
    primaryContainer = md_primaryContainer, onPrimaryContainer = md_onPrimaryContainer,
    secondary = md_secondary, onSecondary = md_onSecondary,
    secondaryContainer = md_secondaryContainer, onSecondaryContainer = md_onSecondaryContainer,
    tertiary = md_tertiary, onTertiary = md_onTertiary,
    tertiaryContainer = md_tertiaryContainer, onTertiaryContainer = md_onTertiaryContainer,
    error = md_error, onError = md_onError,
    errorContainer = md_errorContainer, onErrorContainer = md_onErrorContainer,
    background = md_background, onBackground = md_onBackground,
    surface = md_surface, onSurface = md_onSurface,
    surfaceVariant = md_surfaceVariant, onSurfaceVariant = md_onSurfaceVariant,
    outline = md_outline, outlineVariant = md_outlineVariant,
    inverseSurface = md_inverseSurface, inverseOnSurface = md_inverseOnSurface,
    inversePrimary = md_inversePrimary,
)

internal val DarkColors = darkColorScheme(
    primary = dk_primary, onPrimary = dk_onPrimary,
    primaryContainer = dk_primaryContainer, onPrimaryContainer = dk_onPrimaryContainer,
    secondary = dk_secondary, onSecondary = dk_onSecondary,
    secondaryContainer = dk_secondaryContainer, onSecondaryContainer = dk_onSecondaryContainer,
    tertiary = dk_tertiary, onTertiary = dk_onTertiary,
    tertiaryContainer = dk_tertiaryContainer, onTertiaryContainer = dk_onTertiaryContainer,
    error = dk_error, onError = dk_onError,
    errorContainer = dk_errorContainer, onErrorContainer = dk_onErrorContainer,
    background = dk_background, onBackground = dk_onBackground,
    surface = dk_surface, onSurface = dk_onSurface,
    surfaceVariant = dk_surfaceVariant, onSurfaceVariant = dk_onSurfaceVariant,
    outline = dk_outline, outlineVariant = dk_outlineVariant,
    inverseSurface = dk_inverseSurface, inverseOnSurface = dk_inverseOnSurface,
    inversePrimary = dk_inversePrimary,
)
