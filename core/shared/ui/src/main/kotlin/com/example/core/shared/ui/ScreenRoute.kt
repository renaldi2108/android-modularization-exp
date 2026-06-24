package com.example.core.shared.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.utils.viewmodel.EventViewModel
import com.example.core.utils.viewmodel.StateViewModel

@Composable
inline fun <reified VM, S, E> ScreenRoute(
    noinline onEvent: suspend (event: E, snackbar: SnackbarHostState) -> Unit = { _, _ -> },
    content: @Composable (state: S, snackbar: SnackbarHostState, viewModel: VM) -> Unit,
) where VM : ViewModel, VM : StateViewModel<S>, VM : EventViewModel<E> {
    val viewModel: VM = hiltViewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event -> onEvent(event, snackbarHostState) }

    content(state, snackbarHostState, viewModel)
}
