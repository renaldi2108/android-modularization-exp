package com.example.app.feature.auth.domain

import com.example.app.core.common.handler.BaseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AuthHandler(
    private val repo: AuthRepository,
    scope: CoroutineScope
) : BaseHandler<AuthState, AuthEvent>(AuthState.Idle, scope) {

    fun loginWithEmail(email: String, password: String) {
        scope.launch {
            setState { AuthState.Loading }
            runCatching { repo.loginWithEmail(email, password) }
                .onSuccess { user ->
                    setState { AuthState.Success(user) }
                    emitEvent(AuthEvent.NavigateToDashboard)
                }
                .onFailure { e ->
                    setState { AuthState.Error(e.message ?: "Login gagal", e) }
                    emitEvent(AuthEvent.ShowError(e.message ?: "Login gagal"))
                }
        }
    }

    fun logout() {
        scope.launch {
            runCatching { repo.logout() }
                .onSuccess {
                    setState { AuthState.Idle }
                    emitEvent(AuthEvent.SessionExpired)
                }
                .onFailure { e ->
                    emitEvent(AuthEvent.ShowError(e.message ?: "Logout gagal"))
                }
        }
    }

    fun checkSession() {
        val user = repo.getCurrentUser()
        setState { if (user != null) AuthState.Success(user) else AuthState.Idle }
    }

    fun reset() { setState { AuthState.Idle } }
}
