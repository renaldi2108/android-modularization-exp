package com.example.feature.todos.domain

import com.example.core.utils.handler.BaseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class TodoListHandler(
    private val repo: TodoRepository,
    scope: CoroutineScope,
) : BaseHandler<TodoListState, TodosEvent>(TodoListState.Loading, scope) {

    fun load() = scope.launch {
        setState { TodoListState.Loading }
        runCatching { repo.getTodos() }
            .onSuccess { setState { TodoListState.Success(it) } }
            .onFailure { e ->
                val msg = e.message ?: "Gagal memuat todo"
                setState { TodoListState.Error(msg) }
                emitEvent(TodosEvent.ShowError(msg))
            }
    }
}

class TodoDetailHandler(
    private val repo: TodoRepository,
    scope: CoroutineScope,
) : BaseHandler<TodoDetailState, TodosEvent>(TodoDetailState.Loading, scope) {

    fun load(id: Int) = scope.launch {
        setState { TodoDetailState.Loading }
        runCatching { repo.getTodo(id) }
            .onSuccess { setState { TodoDetailState.Success(it) } }
            .onFailure { e ->
                val msg = e.message ?: "Gagal memuat detail"
                setState { TodoDetailState.Error(msg) }
                emitEvent(TodosEvent.ShowError(msg))
            }
    }
}
