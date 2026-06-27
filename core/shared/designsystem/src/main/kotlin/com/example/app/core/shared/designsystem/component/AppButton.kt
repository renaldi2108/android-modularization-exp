package com.example.app.core.shared.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.app.core.shared.designsystem.theme.AppTheme

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    Button(
        onClick  = onClick,
        enabled  = enabled && !loading,
        modifier = modifier.fillMaxWidth().height(AppTheme.dimens.buttonHeight),
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier    = Modifier.size(AppTheme.dimens.iconSizeSmall),
                strokeWidth = 2.dp,
                color       = AppTheme.colors.onPrimary,
            )
        } else {
            Text(text, style = AppTheme.typography.labelLarge)
        }
    }
}
