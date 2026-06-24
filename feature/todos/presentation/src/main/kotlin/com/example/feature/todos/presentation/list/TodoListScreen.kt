package com.example.feature.todos.presentation.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.core.shared.designsystem.component.AppButton
import com.example.core.shared.designsystem.theme.AppTheme
import com.example.feature.todos.domain.Todo

@Composable
fun TodoListScreen(
    state: TodoListUiState,
    onAction: (TodoListAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    onTodoClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier = modifier, snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                state.isLoading && state.todos.isEmpty() ->
                    Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }

                state.errorMessage != null && state.todos.isEmpty() ->
                    Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(state.errorMessage, color = AppTheme.colors.error)
                            AppButton("Coba lagi", { onAction(TodoListAction.Retry) }, Modifier.padding(top = AppTheme.dimens.space))
                        }
                    }

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(AppTheme.dimens.space),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.spaceSm),
                ) {
                    items(state.todos, key = { it.id }) { todo ->
                        TodoRow(todo) { onTodoClick(todo.id) }
                    }
                }
            }
        }
    }
}

@Composable
private fun TodoRow(todo: Todo, onClick: () -> Unit) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(AppTheme.dimens.space),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.spaceSm),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (todo.completed) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (todo.completed) AppTheme.colors.primary else AppTheme.colors.onSurfaceVariant,
            )
            Text(todo.todo, style = AppTheme.typography.bodyMedium)
        }
    }
}
