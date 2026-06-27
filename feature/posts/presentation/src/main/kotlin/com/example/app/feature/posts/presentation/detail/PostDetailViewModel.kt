package com.example.app.feature.posts.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.app.core.common.uistate.MultiSourceUiStateHolderBuilder
import com.example.app.core.utils.viewmodel.BaseViewModel
import com.example.app.core.utils.viewmodel.EventViewModel
import com.example.app.feature.posts.domain.PostDetailHandler
import com.example.app.feature.posts.domain.PostDetailState
import com.example.app.feature.posts.domain.PostRepository
import com.example.app.feature.posts.domain.PostsEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    repository: PostRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<PostDetailUiState, PostDetailAction>(), EventViewModel<PostsEvent> {

    private val postId: Int = checkNotNull(savedStateHandle.get<String>("id")).toInt()
    private val handler = PostDetailHandler(repository, viewModelScope)
    override val events: Flow<PostsEvent> = handler.events

    override fun initialState() = PostDetailUiState(isLoading = true)

    override fun MultiSourceUiStateHolderBuilder<PostDetailUiState>.setupHolder() {
        source(handler.state) { domain, current ->
            when (domain) {
                PostDetailState.Loading   -> current.copy(isLoading = true, errorMessage = null)
                is PostDetailState.Success -> current.copy(isLoading = false, post = domain.post, errorMessage = null)
                is PostDetailState.Error   -> current.copy(isLoading = false, errorMessage = domain.message)
            }
        }
    }

    init { handler.load(postId) }

    override fun onAction(action: PostDetailAction) = when (action) {
        PostDetailAction.Retry -> { handler.load(postId); Unit }
    }
}
