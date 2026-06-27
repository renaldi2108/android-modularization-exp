package com.example.app.feature.todos.presentation.list

import androidx.lifecycle.viewModelScope
import com.example.app.core.common.uistate.MultiSourceUiStateHolderBuilder
import com.example.app.core.utils.viewmodel.BaseViewModel
import com.example.app.core.utils.viewmodel.EventViewModel
import com.example.app.feature.todos.domain.TodoListHandler
import com.example.app.feature.todos.domain.TodoListState
import com.example.app.feature.todos.domain.TodoRepository
import com.example.app.feature.todos.domain.TodosEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    repository: TodoRepository,
) : BaseViewModel<TodoListUiState, TodoListAction>(), EventViewModel<TodosEvent> {

    private val handler = TodoListHandler(repository, viewModelScope)
    override val events: Flow<TodosEvent> = handler.events

    override fun initialState() = TodoListUiState(isLoading = true)

    override fun MultiSourceUiStateHolderBuilder<TodoListUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                TodoListState.Loading   -> current.copy(isLoading = true, errorMessage = null)
                is TodoListState.Success -> current.copy(isLoading = false, todos = domain.todos.toImmutableList(), errorMessage = null)
                is TodoListState.Error   -> current.copy(isLoading = false, errorMessage = domain.message)
            }
        }
    }

    init { handler.load() }

    override fun onAction(action: TodoListAction) = when (action) {
        TodoListAction.Retry -> { handler.load(); Unit }
    }
}
