package com.example.app.feature.carts.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.app.core.shared.ui.ScreenRoute
import com.example.app.core.utils.navigation.AppDestinations
import com.example.app.feature.carts.domain.CartsEvent
import com.example.app.feature.carts.presentation.detail.CartDetailScreen
import com.example.app.feature.carts.presentation.detail.CartDetailUiState
import com.example.app.feature.carts.presentation.detail.CartDetailViewModel
import com.example.app.feature.carts.presentation.list.CartListScreen
import com.example.app.feature.carts.presentation.list.CartListUiState
import com.example.app.feature.carts.presentation.list.CartListViewModel

object CartRoutes {
    const val List = AppDestinations.Carts
    const val DetailPattern = "cart/{id}"
    fun detail(id: Int) = "cart/$id"
}

fun NavGraphBuilder.cartsNavGraph(navController: NavHostController) {
    composable(CartRoutes.List) {
        ScreenRoute<CartListViewModel, CartListUiState, CartsEvent>(
            onEvent = { event, snackbar -> when (event) { is CartsEvent.ShowError -> snackbar.showSnackbar(event.message) } },
        ) { state, snackbar, viewModel ->
            CartListScreen(state, viewModel::onAction, snackbar, onCartClick = { id -> navController.navigate(CartRoutes.detail(id)) })
        }
    }
    composable(CartRoutes.DetailPattern, arguments = listOf(navArgument("id") { type = NavType.StringType })) {
        ScreenRoute<CartDetailViewModel, CartDetailUiState, CartsEvent>(
            onEvent = { event, snackbar -> when (event) { is CartsEvent.ShowError -> snackbar.showSnackbar(event.message) } },
        ) { state, snackbar, viewModel ->
            CartDetailScreen(state, viewModel::onAction, snackbar, onBack = { navController.popBackStack() })
        }
    }
}
