package com.example.feature.recipes.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.core.shared.ui.ScreenRoute
import com.example.core.utils.navigation.AppDestinations
import com.example.feature.recipes.domain.RecipesEvent
import com.example.feature.recipes.presentation.detail.RecipeDetailScreen
import com.example.feature.recipes.presentation.detail.RecipeDetailUiState
import com.example.feature.recipes.presentation.detail.RecipeDetailViewModel
import com.example.feature.recipes.presentation.list.RecipeListScreen
import com.example.feature.recipes.presentation.list.RecipeListUiState
import com.example.feature.recipes.presentation.list.RecipeListViewModel

object RecipeRoutes {
    const val List = AppDestinations.Recipes
    const val DetailPattern = "recipe/{id}"
    fun detail(id: Int) = "recipe/$id"
}

fun NavGraphBuilder.recipesNavGraph(navController: NavHostController) {
    composable(RecipeRoutes.List) {
        ScreenRoute<RecipeListViewModel, RecipeListUiState, RecipesEvent>(
            onEvent = { event, snackbar -> when (event) { is RecipesEvent.ShowError -> snackbar.showSnackbar(event.message) } },
        ) { state, snackbar, viewModel ->
            RecipeListScreen(state, viewModel::onAction, snackbar, onRecipeClick = { id -> navController.navigate(RecipeRoutes.detail(id)) })
        }
    }
    composable(RecipeRoutes.DetailPattern, arguments = listOf(navArgument("id") { type = NavType.StringType })) {
        ScreenRoute<RecipeDetailViewModel, RecipeDetailUiState, RecipesEvent>(
            onEvent = { event, snackbar -> when (event) { is RecipesEvent.ShowError -> snackbar.showSnackbar(event.message) } },
        ) { state, snackbar, viewModel ->
            RecipeDetailScreen(state, viewModel::onAction, snackbar, onBack = { navController.popBackStack() })
        }
    }
}
