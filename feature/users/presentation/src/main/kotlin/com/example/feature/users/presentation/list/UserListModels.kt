package com.example.feature.users.presentation.list

import androidx.compose.runtime.Immutable
import com.example.feature.users.domain.AppUser
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class UserListUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val users: ImmutableList<AppUser> = persistentListOf(),
    val errorMessage: String? = null,
)

sealed interface UserListAction {
    data class QueryChanged(val value: String) : UserListAction
    data object Submit : UserListAction
    data object Retry : UserListAction
}
