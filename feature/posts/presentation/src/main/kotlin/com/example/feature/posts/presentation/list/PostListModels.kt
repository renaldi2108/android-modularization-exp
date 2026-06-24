package com.example.feature.posts.presentation.list

import androidx.compose.runtime.Immutable
import com.example.feature.posts.domain.Post
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class PostListUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val posts: ImmutableList<Post> = persistentListOf(),
    val errorMessage: String? = null,
)

sealed interface PostListAction {
    data class QueryChanged(val value: String) : PostListAction
    data object Submit : PostListAction
    data object Retry : PostListAction
}
