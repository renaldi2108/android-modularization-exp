package com.example.feature.users.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.core.shared.ui.ScreenRoute
import com.example.core.utils.navigation.AppDestinations
import com.example.feature.users.domain.UsersEvent
import com.example.feature.users.presentation.detail.UserDetailScreen
import com.example.feature.users.presentation.detail.UserDetailUiState
import com.example.feature.users.presentation.detail.UserDetailViewModel
import com.example.feature.users.presentation.list.UserListScreen
import com.example.feature.users.presentation.list.UserListUiState
import com.example.feature.users.presentation.list.UserListViewModel

object UserRoutes {
    const val List = AppDestinations.Users
    const val DetailPattern = "user/{id}"
    fun detail(id: Int) = "user/$id"
}

fun NavGraphBuilder.usersNavGraph(navController: NavHostController) {
    composable(UserRoutes.List) {
        ScreenRoute<UserListViewModel, UserListUiState, UsersEvent>(
            onEvent = { event, snackbar -> when (event) { is UsersEvent.ShowError -> snackbar.showSnackbar(event.message) } },
        ) { state, snackbar, viewModel ->
            UserListScreen(state, viewModel::onAction, snackbar, onUserClick = { id -> navController.navigate(UserRoutes.detail(id)) })
        }
    }
    composable(UserRoutes.DetailPattern, arguments = listOf(navArgument("id") { type = NavType.StringType })) {
        ScreenRoute<UserDetailViewModel, UserDetailUiState, UsersEvent>(
            onEvent = { event, snackbar -> when (event) { is UsersEvent.ShowError -> snackbar.showSnackbar(event.message) } },
        ) { state, snackbar, viewModel ->
            UserDetailScreen(state, viewModel::onAction, snackbar, onBack = { navController.popBackStack() })
        }
    }
}
