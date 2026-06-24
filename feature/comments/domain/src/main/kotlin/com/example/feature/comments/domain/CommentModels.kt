package com.example.feature.comments.domain

data class Comment(
    val id: Int,
    val body: String,
    val postId: Int,
    val userName: String,
)

sealed interface CommentListState {
    data object Loading : CommentListState
    data class Success(val comments: List<Comment>) : CommentListState
    data class Error(val message: String) : CommentListState
}

sealed interface CommentsEvent {
    data class ShowError(val message: String) : CommentsEvent
}
