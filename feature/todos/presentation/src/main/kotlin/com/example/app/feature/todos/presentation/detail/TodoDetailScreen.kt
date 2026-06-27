package com.example.app.feature.todos.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.app.core.shared.designsystem.theme.AppTheme
import com.example.app.feature.todos.domain.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    state: TodoDetailUiState,
    onAction: (TodoDetailAction) -> Unit,
    snackbarHostState: SnackbarHostState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Detail Todo", maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Kembali")
                    }
                },
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), Alignment.Center) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.todo != null -> TodoDetailContent(state.todo)
                state.errorMessage != null -> Text(state.errorMessage, color = AppTheme.colors.error)
            }
        }
    }
}

@Composable
private fun TodoDetailContent(todo: Todo) {
    Column(
        modifier = Modifier.fillMaxSize().padding(AppTheme.dimens.screenPadding),
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.spaceSm),
    ) {
        Text(todo.todo, style = AppTheme.typography.titleLarge)
        Text(
            if (todo.completed) "Status: Selesai ✓" else "Status: Belum selesai",
            style = AppTheme.typography.bodyMedium,
            color = if (todo.completed) AppTheme.colors.primary else AppTheme.colors.onSurfaceVariant,
        )
        Text("User ID: ${todo.userId}", style = AppTheme.typography.bodyMedium)
    }
}
