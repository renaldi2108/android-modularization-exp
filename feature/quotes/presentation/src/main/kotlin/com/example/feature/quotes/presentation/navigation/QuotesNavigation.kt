package com.example.feature.quotes.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.core.shared.ui.ScreenRoute
import com.example.core.utils.navigation.AppDestinations
import com.example.feature.quotes.domain.QuotesEvent
import com.example.feature.quotes.presentation.list.QuoteListScreen
import com.example.feature.quotes.presentation.list.QuoteListUiState
import com.example.feature.quotes.presentation.list.QuoteListViewModel

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
