package com.example.feature.auth.domain

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val photoUrl: String?,
    val token: String
)

sealed class AuthState {
    object Idle    : AuthState()
    object Loading : AuthState()
    data class Success(val user: User) : AuthState()
    data class Error(val message: String, val cause: Throwable? = null) : AuthState()
}

sealed class AuthEvent {
    object NavigateToDashboard              : AuthEvent()
    object SessionExpired                   : AuthEvent()
    data class ShowError(val message: String) : AuthEvent()
}
