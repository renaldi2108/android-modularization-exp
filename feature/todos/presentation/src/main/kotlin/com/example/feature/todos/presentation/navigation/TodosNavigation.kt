package com.example.feature.todos.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.core.shared.ui.ScreenRoute
import com.example.core.utils.navigation.AppDestinations
import com.example.feature.todos.domain.TodosEvent
import com.example.feature.todos.presentation.detail.TodoDetailScreen
import com.example.feature.todos.presentation.detail.TodoDetailUiState
import com.example.feature.todos.presentation.detail.TodoDetailViewModel
import com.example.feature.todos.presentation.list.TodoListScreen
import com.example.feature.todos.presentation.list.TodoListUiState
import com.example.feature.todos.presentation.list.TodoListViewModel

object TodoRoutes {
    const val List = AppDestinations.Todos
    const val DetailPattern = "todo/{id}"
    fun detail(id: Int) = "todo/$id"
}

fun NavGraphBuilder.todosNavGraph(navController: NavHostController) {
    composable(TodoRoutes.List) {
        ScreenRoute<TodoListViewModel, TodoListUiState, TodosEvent>(
            onEvent = { event, snackbar -> when (event) { is TodosEvent.ShowError -> snackbar.showSnackbar(event.message) } },
        ) { state, snackbar, viewModel ->
            TodoListScreen(state, viewModel::onAction, snackbar, onTodoClick = { id -> navController.navigate(TodoRoutes.detail(id)) })
        }
    }
    composable(TodoRoutes.DetailPattern, arguments = listOf(navArgument("id") { type = NavType.StringType })) {
        ScreenRoute<TodoDetailViewModel, TodoDetailUiState, TodosEvent>(
            onEvent = { event, snackbar -> when (event) { is TodosEvent.ShowError -> snackbar.showSnackbar(event.message) } },
        ) { state, snackbar, viewModel ->
            TodoDetailScreen(state, viewModel::onAction, snackbar, onBack = { navController.popBackStack() })
        }
    }
}
