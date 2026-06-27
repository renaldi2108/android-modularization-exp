package com.example.app.feature.comments.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.app.core.shared.ui.ScreenRoute
import com.example.app.core.utils.navigation.AppDestinations
import com.example.app.feature.comments.domain.CommentsEvent
import com.example.app.feature.comments.presentation.list.CommentListScreen
import com.example.app.feature.comments.presentation.list.CommentListUiState
import com.example.app.feature.comments.presentation.list.CommentListViewModel

object CommentRoutes {
    const val List = AppDestinations.Comments
}

fun NavGraphBuilder.commentsNavGraph(@Suppress("UNUSED_PARAMETER") navController: NavHostController) {
    composable(CommentRoutes.List) {
        ScreenRoute<CommentListViewModel, CommentListUiState, CommentsEvent>(
            onEvent = { event, snackbar -> when (event) { is CommentsEvent.ShowError -> snackbar.showSnackbar(event.message) } },
        ) { state, snackbar, viewModel ->
            CommentListScreen(state, viewModel::onAction, snackbar)
        }
    }
}
