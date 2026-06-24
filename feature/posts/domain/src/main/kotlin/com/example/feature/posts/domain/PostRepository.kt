package com.example.feature.posts.domain

interface PostRepository {
    suspend fun getPosts(limit: Int = 20): List<Post>
    suspend fun searchPosts(query: String): List<Post>
    suspend fun getPost(id: Int): Post
}
