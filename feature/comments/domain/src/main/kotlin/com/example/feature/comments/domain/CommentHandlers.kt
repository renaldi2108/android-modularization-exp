package com.example.feature.comments.domain

import com.example.core.utils.handler.BaseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
