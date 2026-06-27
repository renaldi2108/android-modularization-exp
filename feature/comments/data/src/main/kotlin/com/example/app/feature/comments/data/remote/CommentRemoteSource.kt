package com.example.app.feature.comments.data.remote

import com.example.app.core.network.remote.ApiRequest
import com.example.app.core.network.remote.RemoteDataSource
import com.example.app.core.network.remote.fetch
import com.example.app.core.common.result.Result
import com.squareup.moshi.JsonClass
import javax.inject.Inject

@JsonClass(generateAdapter = true)
data class CommentUserDto(
    val id: Int = 0,
    val username: String = "",
    val fullName: String = "",
)

@JsonClass(generateAdapter = true)
data class CommentDto(
    val id: Int,
    val body: String = "",
    val postId: Int = 0,
    val user: CommentUserDto = CommentUserDto(),
)

@JsonClass(generateAdapter = true)
data class CommentsResponse(
    val comments: List<CommentDto> = emptyList(),
    val total: Int = 0,
    val skip: Int = 0,
    val limit: Int = 0,
)

interface CommentRemoteSource {
    suspend fun getComments(limit: Int): Result<CommentsResponse>
}

class CommentRemoteSourceImpl @Inject constructor(
    private val remote: RemoteDataSource,
) : CommentRemoteSource {
    override suspend fun getComments(limit: Int): Result<CommentsResponse> =
        remote.fetch(ApiRequest.get("comments", query = mapOf("limit" to limit.toString())))
}
