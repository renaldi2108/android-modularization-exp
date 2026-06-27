package com.example.app.feature.posts.domain

import com.example.app.core.common.handler.BaseHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PostListHandler(
    private val repo: PostRepository,
    scope: CoroutineScope,
) : BaseHandler<PostListState, PostsEvent>(PostListState.Loading, scope) {

    fun load() = scope.launch {
        setState { PostListState.Loading }
        runCatching { repo.getPosts() }
            .onSuccess { setState { PostListState.Success(it) } }
            .onFailure { fail(it) }
    }

    fun search(query: String) = scope.launch {
        setState { PostListState.Loading }
        runCatching { if (query.isBlank()) repo.getPosts() else repo.searchPosts(query) }
            .onSuccess { setState { PostListState.Success(it) } }
            .onFailure { fail(it) }
    }

    private suspend fun fail(e: Throwable) {
        val msg = e.message ?: "Gagal memuat post"
        setState { PostListState.Error(msg) }
        emitEvent(PostsEvent.ShowError(msg))
    }
}

class PostDetailHandler(
    private val repo: PostRepository,
    scope: CoroutineScope,
) : BaseHandler<PostDetailState, PostsEvent>(PostDetailState.Loading, scope) {

    fun load(id: Int) = scope.launch {
        setState { PostDetailState.Loading }
        runCatching { repo.getPost(id) }
            .onSuccess { setState { PostDetailState.Success(it) } }
            .onFailure { e ->
                val msg = e.message ?: "Gagal memuat detail"
                setState { PostDetailState.Error(msg) }
                emitEvent(PostsEvent.ShowError(msg))
            }
    }
}
