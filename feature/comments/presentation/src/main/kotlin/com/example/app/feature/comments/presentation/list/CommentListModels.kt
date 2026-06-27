package com.example.app.feature.comments.presentation.list

import androidx.compose.runtime.Immutable
import com.example.app.feature.comments.domain.Comment
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class CommentListUiState(
    val isLoading: Boolean = false,
    val comments: ImmutableList<Comment> = persistentListOf(),
    val errorMessage: String? = null,
)

sealed interface CommentListAction {
    data object Retry : CommentListAction
}
