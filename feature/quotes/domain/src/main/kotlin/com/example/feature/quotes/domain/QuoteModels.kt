package com.example.feature.quotes.domain

import com.example.core.utils.handler.BaseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

interface QuoteRepository {
    suspend fun getQuotes(limit: Int = 30): List<Quote>
}

class QuoteListHandler(
    private val repo: QuoteRepository,
    scope: CoroutineScope,
) : BaseHandler<QuoteListState, QuotesEvent>(QuoteListState.Loading, scope) {

    fun load() = scope.launch {
        setState { QuoteListState.Loading }
        runCatching { repo.getQuotes() }
            .onSuccess { setState { QuoteListState.Success(it) } }
            .onFailure { e ->
                val msg = e.message ?: "Gagal memuat quotes"
                setState { QuoteListState.Error(msg) }
                emitEvent(QuotesEvent.ShowError(msg))
            }
    }
}
