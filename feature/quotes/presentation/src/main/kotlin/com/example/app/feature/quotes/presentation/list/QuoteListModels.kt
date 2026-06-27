package com.example.app.feature.quotes.presentation.list

import androidx.compose.runtime.Immutable
import com.example.app.feature.quotes.domain.Quote
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class QuoteListUiState(
    val isLoading: Boolean = false,
    val quotes: ImmutableList<Quote> = persistentListOf(),
    val errorMessage: String? = null,
)

sealed interface QuoteListAction {
    data object Retry : QuoteListAction
}
