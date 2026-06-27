package com.example.app.feature.quotes.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.app.core.shared.ui.ScreenRoute
import com.example.app.core.utils.navigation.AppDestinations
import com.example.app.feature.quotes.domain.QuotesEvent
import com.example.app.feature.quotes.presentation.list.QuoteListScreen
import com.example.app.feature.quotes.presentation.list.QuoteListUiState
import com.example.app.feature.quotes.presentation.list.QuoteListViewModel

object QuoteRoutes {
    const val List = AppDestinations.Quotes
}

fun NavGraphBuilder.quotesNavGraph(@Suppress("UNUSED_PARAMETER") navController: NavHostController) {
    composable(QuoteRoutes.List) {
        ScreenRoute<QuoteListViewModel, QuoteListUiState, QuotesEvent>(
            onEvent = { event, snackbar -> when (event) { is QuotesEvent.ShowError -> snackbar.showSnackbar(event.message) } },
        ) { state, snackbar, viewModel ->
            QuoteListScreen(state, viewModel::onAction, snackbar)
        }
    }
}
