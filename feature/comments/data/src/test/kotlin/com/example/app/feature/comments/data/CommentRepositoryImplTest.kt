package com.example.app.feature.comments.data

import com.example.app.core.common.result.Result
import com.example.app.feature.comments.data.remote.CommentDto
import com.example.app.feature.comments.data.remote.CommentUserDto
import com.example.app.feature.comments.data.remote.CommentsResponse
import com.example.app.feature.comments.data.remote.CommentRemoteSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CommentRepositoryImplTest {
    private val dto = CommentDto(id = 1, body = "halo", postId = 7, user = CommentUserDto(id = 2, username = "eve", fullName = "Eve Stone"))

    private class FakeRemote(
        var listResult: Result<CommentsResponse> = Result.Success(CommentsResponse()),
    ) : CommentRemoteSource {
        var searchedQuery: String? = null
        override suspend fun getComments(limit: Int) = listResult
    }

    @Test
    fun `getComments sukses memetakan dto ke domain`() = runTest {
        val remote = FakeRemote(listResult = Result.Success(CommentsResponse(comments = listOf(dto))))
        val repo = CommentRepositoryImpl(remote)
        val result = repo.getComments()
        assertEquals(1, result.size)
        assertEquals("Eve Stone", result[0].userName)
    }

    @Test
    fun `getComments error melempar IllegalStateException berisi message`() = runTest {
        val repo = CommentRepositoryImpl(FakeRemote(listResult = Result.Error("gagal muat")))
        val thrown = runCatching { repo.getComments() }.exceptionOrNull()
        assertTrue(thrown is IllegalStateException)
        assertEquals("gagal muat", thrown!!.message)
    }

    @Test
    fun `getComments error meneruskan cause asli bila ada`() = runTest {
        val boom = IllegalArgumentException("boom")
        val repo = CommentRepositoryImpl(FakeRemote(listResult = Result.Error("gagal", cause = boom)))
        val thrown = runCatching { repo.getComments() }.exceptionOrNull()
        assertEquals(boom, thrown)
    }
}
