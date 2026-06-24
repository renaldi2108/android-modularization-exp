package com.example.feature.auth.presentation.login

import androidx.compose.runtime.Immutable

@Immutable
data class LoginUiState(
    val username: String        = "",
    val password: String        = "",
    val usernameError: String?  = null,
    val passwordError: String?  = null,
    val passwordVisible: Boolean = false,
    val isLoading: Boolean      = false,
    val isButtonEnabled: Boolean = false
)

sealed class LoginUiAction {
    data class UsernameChanged(val value: String) : LoginUiAction()
    data class PasswordChanged(val value: String) : LoginUiAction()
    object PasswordToggled                        : LoginUiAction()
    object LoginClicked                           : LoginUiAction()
}
