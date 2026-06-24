package com.example.feature.todos.presentation.detail

import androidx.compose.runtime.Immutable
import com.example.feature.todos.domain.Todo

@Immutable
data class TodoDetailUiState(
    val isLoading: Boolean = false,
    val todo: Todo? = null,
    val errorMessage: String? = null,
)

sealed interface TodoDetailAction {
    data object Retry : TodoDetailAction
}
