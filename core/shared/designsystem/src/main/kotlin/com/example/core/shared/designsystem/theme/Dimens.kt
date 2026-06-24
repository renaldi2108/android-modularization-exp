package com.example.core.shared.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Dimens(
    val spaceNone: Dp = 0.dp,
    val spaceXxs: Dp  = 2.dp,
    val spaceXs: Dp   = 4.dp,
    val spaceSm: Dp   = 8.dp,
    val spaceMd: Dp   = 12.dp,
    val space: Dp     = 16.dp,
    val spaceLg: Dp   = 24.dp,
    val spaceXl: Dp   = 32.dp,
    val spaceXxl: Dp  = 48.dp,

    val screenPadding: Dp  = 24.dp,
    val buttonHeight: Dp   = 52.dp,
    val minTouchTarget: Dp = 48.dp,
    val iconSize: Dp       = 24.dp,
    val iconSizeSmall: Dp  = 20.dp,
    val borderWidth: Dp    = 1.dp,
)

val LocalDimens = staticCompositionLocalOf { Dimens() }
