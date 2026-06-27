package com.example.app.core.utils.viewmodel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface StateViewModel<S> {
    val uiState: StateFlow<S>
}

interface EventViewModel<E> {
    val events: Flow<E>
}
