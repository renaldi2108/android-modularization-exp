package com.example.feature.posts.presentation.detail

import androidx.compose.runtime.Immutable
import com.example.feature.posts.domain.Post

@Immutable
data class PostDetailUiState(
    val isLoading: Boolean = false,
    val post: Post? = null,
    val errorMessage: String? = null,
)

sealed interface PostDetailAction {
    data object Retry : PostDetailAction
}
