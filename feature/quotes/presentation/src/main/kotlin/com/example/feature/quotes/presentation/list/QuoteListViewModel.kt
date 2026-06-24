package com.example.feature.quotes.presentation.list

import androidx.lifecycle.viewModelScope
import com.example.core.utils.uistate.MultiSourceUiStateHolderBuilder
import com.example.core.utils.viewmodel.BaseViewModel
import com.example.core.utils.viewmodel.EventViewModel
import com.example.feature.quotes.domain.QuoteListHandler
import com.example.feature.quotes.domain.QuoteListState
import com.example.feature.quotes.domain.QuoteRepository
import com.example.feature.quotes.domain.QuotesEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class QuoteListViewModel @Inject constructor(
    repository: QuoteRepository,
) : BaseViewModel<QuoteListUiState, QuoteListAction>(), EventViewModel<QuotesEvent> {

    private val handler = QuoteListHandler(repository, viewModelScope)
    override val events: Flow<QuotesEvent> = handler.events

    override fun initialState() = QuoteListUiState(isLoading = true)

    override fun MultiSourceUiStateHolderBuilder<QuoteListUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                QuoteListState.Loading   -> current.copy(isLoading = true, errorMessage = null)
                is QuoteListState.Success -> current.copy(isLoading = false, quotes = domain.quotes.toImmutableList(), errorMessage = null)
                is QuoteListState.Error   -> current.copy(isLoading = false, errorMessage = domain.message)
            }
        }
    }

    init { handler.load() }

    override fun onAction(action: QuoteListAction) = when (action) {
        QuoteListAction.Retry -> { handler.load(); Unit }
    }
}
