package com.example.app.feature.posts.domain

data class Post(
    val id: Int,
    val title: String,
    val body: String,
    val tags: List<String>,
    val userId: Int,
    val views: Int,
    val likes: Int,
    val dislikes: Int,
)

sealed interface PostListState {
    data object Loading : PostListState
    data class Success(val posts: List<Post>) : PostListState
    data class Error(val message: String) : PostListState
}

sealed interface PostDetailState {
    data object Loading : PostDetailState
    data class Success(val post: Post) : PostDetailState
    data class Error(val message: String) : PostDetailState
}

sealed interface PostsEvent {
    data class ShowError(val message: String) : PostsEvent
}
