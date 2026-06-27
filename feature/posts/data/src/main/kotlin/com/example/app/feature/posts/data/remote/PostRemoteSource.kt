package com.example.app.feature.posts.data.remote

import com.example.app.core.network.remote.ApiRequest
import com.example.app.core.network.remote.RemoteDataSource
import com.example.app.core.network.remote.fetch
import com.example.app.core.common.result.Result
import com.squareup.moshi.JsonClass
import javax.inject.Inject

@JsonClass(generateAdapter = true)
data class ReactionsDto(
    val likes: Int = 0,
    val dislikes: Int = 0,
)

@JsonClass(generateAdapter = true)
data class PostDto(
    val id: Int,
    val title: String = "",
    val body: String = "",
    val tags: List<String> = emptyList(),
    val userId: Int = 0,
    val views: Int = 0,
    val reactions: ReactionsDto = ReactionsDto(),
)

@JsonClass(generateAdapter = true)
data class PostsResponse(
    val posts: List<PostDto> = emptyList(),
    val total: Int = 0,
    val skip: Int = 0,
    val limit: Int = 0,
)

interface PostRemoteSource {
    suspend fun getPosts(limit: Int): Result<PostsResponse>
    suspend fun searchPosts(query: String): Result<PostsResponse>
    suspend fun getPost(id: Int): Result<PostDto>
}

class PostRemoteSourceImpl @Inject constructor(
    private val remote: RemoteDataSource,
) : PostRemoteSource {

    override suspend fun getPosts(limit: Int): Result<PostsResponse> =
        remote.fetch(ApiRequest.get("posts", query = mapOf("limit" to limit.toString())))

    override suspend fun searchPosts(query: String): Result<PostsResponse> =
        remote.fetch(ApiRequest.get("posts/search", query = mapOf("q" to query)))

    override suspend fun getPost(id: Int): Result<PostDto> =
        remote.fetch(ApiRequest.get("posts/$id"))
}
