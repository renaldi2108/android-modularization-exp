package com.example.app.feature.posts.presentation.list

import androidx.lifecycle.viewModelScope
import com.example.app.core.common.uistate.MultiSourceUiStateHolderBuilder
import com.example.app.core.utils.viewmodel.BaseViewModel
import com.example.app.core.utils.viewmodel.EventViewModel
import com.example.app.feature.posts.domain.PostListHandler
import com.example.app.feature.posts.domain.PostListState
import com.example.app.feature.posts.domain.PostRepository
import com.example.app.feature.posts.domain.PostsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PostListViewModel @Inject constructor(
    repository: PostRepository,
) : BaseViewModel<PostListUiState, PostListAction>(), EventViewModel<PostsEvent> {

    private val handler = PostListHandler(repository, viewModelScope)
    override val events: Flow<PostsEvent> = handler.events

    override fun initialState() = PostListUiState(isLoading = true)

    override fun MultiSourceUiStateHolderBuilder<PostListUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                PostListState.Loading   -> current.copy(isLoading = true, errorMessage = null)
                is PostListState.Success -> current.copy(isLoading = false, posts = domain.posts.toImmutableList(), errorMessage = null)
                is PostListState.Error   -> current.copy(isLoading = false, errorMessage = domain.message)
            }
        }
    }

    init { handler.load() }

    override fun onAction(action: PostListAction) = when (action) {
        is PostListAction.QueryChanged -> updateUi { copy(query = action.value) }
        PostListAction.Submit          -> { handler.search(uiState.value.query); Unit }
        PostListAction.Retry           -> { handler.load(); Unit }
    }
}
