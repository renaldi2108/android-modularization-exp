package com.example.app.feature.todos.domain

data class Todo(
    val id: Int,
    val todo: String,
    val completed: Boolean,
    val userId: Int,
)

sealed interface TodoListState {
    data object Loading : TodoListState
    data class Success(val todos: List<Todo>) : TodoListState
    data class Error(val message: String) : TodoListState
}

sealed interface TodoDetailState {
    data object Loading : TodoDetailState
    data class Success(val todo: Todo) : TodoDetailState
    data class Error(val message: String) : TodoDetailState
}

sealed interface TodosEvent {
    data class ShowError(val message: String) : TodosEvent
}
