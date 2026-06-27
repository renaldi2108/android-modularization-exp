package com.example.app.feature.users.presentation.detail

import androidx.compose.runtime.Immutable
import com.example.app.feature.users.domain.AppUser

@Immutable
data class UserDetailUiState(
    val isLoading: Boolean = false,
    val user: AppUser? = null,
    val errorMessage: String? = null,
)

sealed interface UserDetailAction {
    data object Retry : UserDetailAction
}
