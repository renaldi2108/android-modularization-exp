package com.example.feature.comments.domain

import com.example.core.utils.handler.BaseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

interface CommentRepository {
    suspend fun getComments(limit: Int = 30): List<Comment>
}

class CommentListHandler(
    private val repo: CommentRepository,
    scope: CoroutineScope,
) : BaseHandler<CommentListState, CommentsEvent>(CommentListState.Loading, scope) {

    fun load() = scope.launch {
        setState { CommentListState.Loading }
        runCatching { repo.getComments() }
            .onSuccess { setState { CommentListState.Success(it) } }
            .onFailure { e ->
                val msg = e.message ?: "Gagal memuat komentar"
                setState { CommentListState.Error(msg) }
                emitEvent(CommentsEvent.ShowError(msg))
            }
    }
}
