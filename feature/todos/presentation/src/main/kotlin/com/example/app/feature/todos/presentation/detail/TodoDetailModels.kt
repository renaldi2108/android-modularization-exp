package com.example.app.feature.todos.presentation.detail

import androidx.compose.runtime.Immutable
import com.example.app.feature.todos.domain.Todo

@Immutable
data class TodoDetailUiState(
    val isLoading: Boolean = false,
    val todo: Todo? = null,
    val errorMessage: String? = null,
)

sealed interface TodoDetailAction {
    data object Retry : TodoDetailAction
}
