package com.example.app.feature.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.app.core.shared.designsystem.component.AppButton
import com.example.app.core.shared.designsystem.component.AppPasswordField
import com.example.app.core.shared.designsystem.component.AppTextField
import com.example.app.core.shared.designsystem.theme.AppTheme

@Composable
fun LoginScreen(
    state: LoginUiState,
    onAction: (LoginUiAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier     = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        LoginContent(
            state    = state,
            onAction = onAction,
            modifier = Modifier.padding(padding),
        )
    }
}

@Composable
fun LoginContent(
    state: LoginUiState,
    onAction: (LoginUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier            = modifier.fillMaxSize().padding(horizontal = AppTheme.dimens.screenPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text       = "Selamat datang",
            style      = AppTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text     = "Masuk ke akun Anda",
            style    = AppTheme.typography.bodyMedium,
            color    = AppTheme.colors.onSurfaceVariant,
            modifier = Modifier.padding(top = AppTheme.dimens.spaceSm, bottom = AppTheme.dimens.spaceXl)
        )

        AppTextField(
            value           = state.username,
            onValueChange   = { onAction(LoginUiAction.UsernameChanged(it)) },
            label           = "Username",
            leadingIcon     = Icons.Outlined.Person,
            error           = state.usernameError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction    = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
        )

        Spacer(Modifier.height(AppTheme.dimens.spaceMd))

        AppPasswordField(
            value              = state.password,
            onValueChange      = { onAction(LoginUiAction.PasswordChanged(it)) },
            label              = "Password",
            visible            = state.passwordVisible,
            onToggleVisibility = { onAction(LoginUiAction.PasswordToggled) },
            error              = state.passwordError,
            onImeAction        = {
                focusManager.clearFocus()
                onAction(LoginUiAction.LoginClicked)
            },
        )

        Spacer(Modifier.height(AppTheme.dimens.spaceLg))

        AppButton(
            text    = "Masuk",
            onClick = { onAction(LoginUiAction.LoginClicked) },
            enabled = state.isButtonEnabled,
            loading = state.isLoading,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    AppTheme {
        LoginScreen(
            state             = LoginUiState(
                username        = "emilys",
                isButtonEnabled = true
            ),
            onAction          = {},
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
