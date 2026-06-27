package com.example.app.feature.quotes.domain

data class Quote(
    val id: Int,
    val quote: String,
    val author: String,
)

sealed interface QuoteListState {
    data object Loading : QuoteListState
    data class Success(val quotes: List<Quote>) : QuoteListState
    data class Error(val message: String) : QuoteListState
}

sealed interface QuotesEvent {
    data class ShowError(val message: String) : QuotesEvent
}
