package com.example.app.feature.posts.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.app.core.shared.ui.ScreenRoute
import com.example.app.core.utils.navigation.AppDestinations
import com.example.app.feature.posts.domain.PostsEvent
import com.example.app.feature.posts.presentation.detail.PostDetailScreen
import com.example.app.feature.posts.presentation.detail.PostDetailUiState
import com.example.app.feature.posts.presentation.detail.PostDetailViewModel
import com.example.app.feature.posts.presentation.list.PostListScreen
import com.example.app.feature.posts.presentation.list.PostListUiState
import com.example.app.feature.posts.presentation.list.PostListViewModel

object PostRoutes {
    const val List = AppDestinations.Posts
    const val DetailPattern = "post/{id}"
    fun detail(id: Int) = "post/$id"
}

fun NavGraphBuilder.postsNavGraph(navController: NavHostController) {
    composable(PostRoutes.List) {
        ScreenRoute<PostListViewModel, PostListUiState, PostsEvent>(
            onEvent = { event, snackbar -> when (event) { is PostsEvent.ShowError -> snackbar.showSnackbar(event.message) } },
        ) { state, snackbar, viewModel ->
            PostListScreen(state, viewModel::onAction, snackbar, onPostClick = { id -> navController.navigate(PostRoutes.detail(id)) })
        }
    }
    composable(PostRoutes.DetailPattern, arguments = listOf(navArgument("id") { type = NavType.StringType })) {
        ScreenRoute<PostDetailViewModel, PostDetailUiState, PostsEvent>(
            onEvent = { event, snackbar -> when (event) { is PostsEvent.ShowError -> snackbar.showSnackbar(event.message) } },
        ) { state, snackbar, viewModel ->
            PostDetailScreen(state, viewModel::onAction, snackbar, onBack = { navController.popBackStack() })
        }
    }
}
