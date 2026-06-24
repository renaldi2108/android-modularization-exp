package com.example.feature.products.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.core.shared.ui.ScreenRoute
import com.example.core.utils.navigation.AppDestinations
import com.example.feature.products.domain.ProductsEvent
import com.example.feature.products.presentation.detail.ProductDetailScreen
import com.example.feature.products.presentation.detail.ProductDetailUiState
import com.example.feature.products.presentation.detail.ProductDetailViewModel
import com.example.feature.products.presentation.list.ProductListScreen
import com.example.feature.products.presentation.list.ProductListUiState
import com.example.feature.products.presentation.list.ProductListViewModel

object ProductRoutes {
    const val List = AppDestinations.Products
    const val DetailPattern = "product/{id}"
    fun detail(id: Int) = "product/$id"
}

fun NavGraphBuilder.productsNavGraph(navController: NavHostController) {
    composable(ProductRoutes.List) {
        ScreenRoute<ProductListViewModel, ProductListUiState, ProductsEvent>(
            onEvent = { event, snackbar ->
                when (event) {
                    is ProductsEvent.ShowError -> snackbar.showSnackbar(event.message)
                }
            },
        ) { state, snackbar, viewModel ->
            ProductListScreen(
                state = state,
                onAction = viewModel::onAction,
                snackbarHostState = snackbar,
                onProductClick = { id -> navController.navigate(ProductRoutes.detail(id)) },
            )
        }
    }

    composable(
        route = ProductRoutes.DetailPattern,
        arguments = listOf(navArgument("id") { type = NavType.StringType }),
    ) {
        ScreenRoute<ProductDetailViewModel, ProductDetailUiState, ProductsEvent>(
            onEvent = { event, snackbar ->
                when (event) {
                    is ProductsEvent.ShowError -> snackbar.showSnackbar(event.message)
                }
            },
        ) { state, snackbar, viewModel ->
            ProductDetailScreen(
                state = state,
                onAction = viewModel::onAction,
                snackbarHostState = snackbar,
                onBack = { navController.popBackStack() },
            )
        }
    }
}
