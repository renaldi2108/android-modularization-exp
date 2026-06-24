package com.example.feature.auth.presentation.login

import com.example.core.utils.formstate.FormState
import com.example.core.utils.uistate.MultiSourceUiStateHolderBuilder
import com.example.core.utils.validation.Validators
import com.example.core.utils.viewmodel.BaseViewModel
import com.example.core.utils.viewmodel.EventViewModel
import com.example.feature.auth.domain.AuthEvent
import com.example.feature.auth.domain.AuthHandler
import com.example.feature.auth.domain.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val handler: AuthHandler
) : BaseViewModel<LoginUiState, LoginUiAction>(), EventViewModel<AuthEvent> {
    override val events: Flow<AuthEvent> = handler.events

    private data class LoginForm(val username: String = "", val password: String = "")
    private val form = FormState(LoginForm())

    override fun initialState(): LoginUiState = LoginUiState()

    override fun MultiSourceUiStateHolderBuilder<LoginUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                is AuthState.Loading -> current.copy(isLoading = true, isButtonEnabled = false)
                is AuthState.Error   -> current.copy(isLoading = false)
                is AuthState.Success -> current.copy(isLoading = false)
                else                 -> current
            }
        }
    }

    override fun onAction(action: LoginUiAction) = when (action) {
        is LoginUiAction.UsernameChanged -> onUsernameChanged(action.value)
        is LoginUiAction.PasswordChanged -> onPasswordChanged(action.value)
        is LoginUiAction.PasswordToggled -> updateUi { copy(passwordVisible = !passwordVisible) }
        is LoginUiAction.LoginClicked    -> onLoginClicked()
    }

    private fun onUsernameChanged(username: String) {
        form.update { it.copy(username = username) }
        updateUi {
            copy(
                username        = username,
                usernameError   = null,
                isButtonEnabled = canEnable(copy(username = username, usernameError = null))
            )
        }
    }

    private fun onPasswordChanged(pw: String) {
        form.update { it.copy(password = pw) }
        val err = Validators.password(pw)
        form.setError("password", err)
        updateUi {
            copy(
                password        = pw,
                passwordError   = err,
                isButtonEnabled = canEnable(copy(password = pw, passwordError = err))
            )
        }
    }

    private fun onLoginClicked() {
        val s = uiState.value
        if (!canEnable(s)) return
        handler.loginWithEmail(s.username, s.password)
    }

    private fun canEnable(s: LoginUiState): Boolean = Validators.allValid(
        errors         = listOf(s.usernameError, s.passwordError),
        requiredFilled = listOf(s.username, s.password),
    )
}
