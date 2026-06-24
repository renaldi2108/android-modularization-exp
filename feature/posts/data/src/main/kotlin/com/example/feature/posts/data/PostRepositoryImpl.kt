package com.example.feature.posts.data

import com.example.core.utils.result.Result
import com.example.feature.posts.data.remote.PostDto
import com.example.feature.posts.data.remote.PostRemoteSource
import com.example.feature.posts.domain.Post
import com.example.feature.posts.domain.PostRepository
import javax.inject.Inject

private fun PostDto.toDomain() = Post(
    id = id, title = title, body = body, tags = tags, userId = userId,
    views = views, likes = reactions.likes, dislikes = reactions.dislikes,
)

private fun <T> Result<T>.getOrThrow(): T = when (this) {
    is Result.Success -> data
    is Result.Error   -> throw cause ?: IllegalStateException(message)
}

class PostRepositoryImpl @Inject constructor(
    private val remote: PostRemoteSource,
) : PostRepository {
    override suspend fun getPosts(limit: Int) = remote.getPosts(limit).getOrThrow().posts.map { it.toDomain() }
    override suspend fun searchPosts(query: String) = remote.searchPosts(query).getOrThrow().posts.map { it.toDomain() }
    override suspend fun getPost(id: Int) = remote.getPost(id).getOrThrow().toDomain()
}
