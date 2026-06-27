package com.example.app.core.shared.designsystem.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Immutable
data class SystemBarsConfig(
    val statusBarColor: Color = Color.Transparent,
    val navigationBarColor: Color = Color.Transparent,
    val darkStatusBarIcons: Boolean? = null,
    val darkNavigationBarIcons: Boolean? = null,
)

@Composable
internal fun ApplySystemBars(darkTheme: Boolean, config: SystemBarsConfig) {
    val view = LocalView.current
    if (view.isInEditMode) return
    val activity = view.context.findActivity() ?: return
    val window = activity.window

    val statusIconsDark = config.darkStatusBarIcons ?: !darkTheme
    val navIconsDark = config.darkNavigationBarIcons ?: !darkTheme

    DisposableEffect(darkTheme, config) {
        @Suppress("DEPRECATION")
        run {
            window.statusBarColor = config.statusBarColor.toArgb()
            window.navigationBarColor = config.navigationBarColor.toArgb()
        }
        WindowCompat.getInsetsController(window, view).apply {
            isAppearanceLightStatusBars = statusIconsDark
            isAppearanceLightNavigationBars = navIconsDark
        }
        onDispose { }
    }
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
