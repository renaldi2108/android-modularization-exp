package com.example.feature.comments.data

import com.example.core.network.remote.ApiRequest
import com.example.core.network.remote.RemoteDataSource
import com.example.core.network.remote.fetch
import com.example.core.utils.result.Result
import com.example.feature.comments.domain.Comment
import com.example.feature.comments.domain.CommentRepository
import com.squareup.moshi.JsonClass
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

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

private fun CommentDto.toDomain() = Comment(
    id = id,
    body = body,
    postId = postId,
    userName = user.fullName.ifBlank { user.username },
)

private fun <T> Result<T>.getOrThrow(): T = when (this) {
    is Result.Success -> data
    is Result.Error   -> throw cause ?: IllegalStateException(message)
}

class CommentRepositoryImpl @Inject constructor(
    private val remote: CommentRemoteSource,
) : CommentRepository {
    override suspend fun getComments(limit: Int) = remote.getComments(limit).getOrThrow().comments.map { it.toDomain() }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class CommentDataModule {
    @Binds @Singleton
    abstract fun bindCommentRepository(impl: CommentRepositoryImpl): CommentRepository

    @Binds
    abstract fun bindCommentRemoteSource(impl: CommentRemoteSourceImpl): CommentRemoteSource
}
