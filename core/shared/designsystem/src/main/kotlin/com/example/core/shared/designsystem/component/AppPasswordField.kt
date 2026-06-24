package com.example.core.shared.designsystem.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AppPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visible: Boolean,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = Icons.Outlined.Lock,
    error: String? = null,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {},
) {
    AppTextField(
        value                = value,
        onValueChange        = onValueChange,
        label                = label,
        modifier             = modifier,
        leadingIcon          = leadingIcon,
        error                = error,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions      = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        keyboardActions      = KeyboardActions(onDone = { onImeAction() }, onGo = { onImeAction() }),
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector        = if (visible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                    contentDescription = if (visible) "Sembunyikan password" else "Tampilkan password",
                )
            }
        },
    )
}
