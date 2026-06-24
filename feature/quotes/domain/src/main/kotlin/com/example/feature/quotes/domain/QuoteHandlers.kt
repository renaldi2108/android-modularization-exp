package com.example.feature.quotes.domain

import com.example.core.utils.handler.BaseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
