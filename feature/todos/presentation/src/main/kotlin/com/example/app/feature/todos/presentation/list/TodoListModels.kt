package com.example.app.feature.todos.presentation.list

import androidx.compose.runtime.Immutable
import com.example.app.feature.todos.domain.Todo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class TodoListUiState(
    val isLoading: Boolean = false,
    val todos: ImmutableList<Todo> = persistentListOf(),
    val errorMessage: String? = null,
)

sealed interface TodoListAction {
    data object Retry : TodoListAction
}
