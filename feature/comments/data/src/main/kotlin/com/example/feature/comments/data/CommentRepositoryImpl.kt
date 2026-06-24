package com.example.feature.comments.data

import com.example.core.utils.result.Result
import com.example.feature.comments.data.remote.CommentDto
import com.example.feature.comments.data.remote.CommentRemoteSource
import com.example.feature.comments.domain.Comment
import com.example.feature.comments.domain.CommentRepository
import javax.inject.Inject

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
