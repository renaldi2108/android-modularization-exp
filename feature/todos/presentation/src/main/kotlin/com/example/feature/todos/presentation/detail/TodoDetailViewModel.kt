package com.example.feature.todos.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.core.utils.uistate.MultiSourceUiStateHolderBuilder
import com.example.core.utils.viewmodel.BaseViewModel
import com.example.core.utils.viewmodel.EventViewModel
import com.example.feature.todos.domain.TodoDetailHandler
import com.example.feature.todos.domain.TodoDetailState
import com.example.feature.todos.domain.TodoRepository
import com.example.feature.todos.domain.TodosEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TodoDetailViewModel @Inject constructor(
    repository: TodoRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<TodoDetailUiState, TodoDetailAction>(), EventViewModel<TodosEvent> {

    private val todoId: Int = checkNotNull(savedStateHandle.get<String>("id")).toInt()
    private val handler = TodoDetailHandler(repository, viewModelScope)
    override val events: Flow<TodosEvent> = handler.events

    override fun initialState() = TodoDetailUiState(isLoading = true)

    override fun MultiSourceUiStateHolderBuilder<TodoDetailUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                TodoDetailState.Loading   -> current.copy(isLoading = true, errorMessage = null)
                is TodoDetailState.Success -> current.copy(isLoading = false, todo = domain.todo, errorMessage = null)
                is TodoDetailState.Error   -> current.copy(isLoading = false, errorMessage = domain.message)
            }
        }
    }

    init { handler.load(todoId) }

    override fun onAction(action: TodoDetailAction) = when (action) {
        TodoDetailAction.Retry -> { handler.load(todoId); Unit }
    }
}
