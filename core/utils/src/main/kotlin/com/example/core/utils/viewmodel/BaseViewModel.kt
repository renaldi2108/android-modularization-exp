package com.example.core.utils.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.utils.uistate.MultiSourceUiStateHolder
import com.example.core.utils.uistate.MultiSourceUiStateHolderBuilder
import com.example.core.utils.uistate.multiSourceUiState
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<State, Action> : ViewModel(), StateViewModel<State> {
    abstract fun initialState(): State

    abstract fun MultiSourceUiStateHolderBuilder<State>.setupHolder()
    abstract fun onAction(action: Action)

    private val holder: MultiSourceUiStateHolder<State> by lazy {
        viewModelScope.multiSourceUiState {
            initialState(initialState())
            setupHolder()
        }
    }

    override val uiState: StateFlow<State> get() = holder.state

    protected fun updateUi(reduce: State.() -> State) = holder.update(reduce)
}
