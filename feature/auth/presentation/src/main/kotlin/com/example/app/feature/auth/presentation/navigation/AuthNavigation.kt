package com.example.app.feature.auth.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.app.core.shared.ui.ScreenRoute
import com.example.app.core.utils.navigation.AppDestinations
import com.example.app.feature.auth.domain.AuthEvent
import com.example.app.feature.auth.presentation.login.LoginScreen
import com.example.app.feature.auth.presentation.login.LoginUiState
import com.example.app.feature.auth.presentation.login.LoginViewModel

object AuthRoutes {
    const val Login = "auth/login"
}

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    composable(AuthRoutes.Login) {
        ScreenRoute<LoginViewModel, LoginUiState, AuthEvent>(
            onEvent = { event, snackbar ->
                when (event) {
                    AuthEvent.NavigateToDashboard ->
                        navController.navigate(AppDestinations.Dashboard) {
                            popUpTo(AuthRoutes.Login) { inclusive = true }
                        }

                    is AuthEvent.ShowError -> snackbar.showSnackbar(event.message)

                    AuthEvent.SessionExpired -> Unit
                }
            },
        ) { state, snackbar, viewModel ->
            LoginScreen(
                state             = state,
                onAction          = viewModel::onAction,
                snackbarHostState = snackbar,
            )
        }
    }
}
