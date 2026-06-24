package com.example.feature.comments.presentation.list

import androidx.lifecycle.viewModelScope
import com.example.core.utils.uistate.MultiSourceUiStateHolderBuilder
import com.example.core.utils.viewmodel.BaseViewModel
import com.example.core.utils.viewmodel.EventViewModel
import com.example.feature.comments.domain.CommentListHandler
import com.example.feature.comments.domain.CommentListState
import com.example.feature.comments.domain.CommentRepository
import com.example.feature.comments.domain.CommentsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CommentListViewModel @Inject constructor(
    repository: CommentRepository,
) : BaseViewModel<CommentListUiState, CommentListAction>(), EventViewModel<CommentsEvent> {

    private val handler = CommentListHandler(repository, viewModelScope)
    override val events: Flow<CommentsEvent> = handler.events

    override fun initialState() = CommentListUiState(isLoading = true)

    override fun MultiSourceUiStateHolderBuilder<CommentListUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                CommentListState.Loading   -> current.copy(isLoading = true, errorMessage = null)
                is CommentListState.Success -> current.copy(isLoading = false, comments = domain.comments.toImmutableList(), errorMessage = null)
                is CommentListState.Error   -> current.copy(isLoading = false, errorMessage = domain.message)
            }
        }
    }

    init { handler.load() }

    override fun onAction(action: CommentListAction) = when (action) {
        CommentListAction.Retry -> { handler.load(); Unit }
    }
}
