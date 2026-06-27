package com.example.app.feature.comments.domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommentHandlersTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val sample = Comment(id = 1, body = "halo", postId = 7, userName = "Eve Stone")

    private class FakeRepository(
        private val items: List<Comment> = emptyList(),
        private val error: Throwable? = null,
    ) : CommentRepository {
        var searchedQuery: String? = null
        override suspend fun getComments(limit: Int): List<Comment> { error?.let { throw it }; return items }
    }

    @Test
    fun `load sukses menghasilkan Success`() = runTest(dispatcher) {
        val handler = CommentListHandler(FakeRepository(items = listOf(sample)), backgroundScope)
        handler.load()
        advanceUntilIdle()
        assertEquals(CommentListState.Success(listOf(sample)), handler.state.value)
    }

    @Test
    fun `load gagal menghasilkan Error dan ShowError`() = runTest(dispatcher) {
        val handler = CommentListHandler(FakeRepository(error = IllegalStateException("boom")), backgroundScope)
        val events = mutableListOf<CommentsEvent>()
        handler.events.onEach { events += it }.launchIn(backgroundScope)
        handler.load()
        advanceUntilIdle()
        val state = handler.state.value
        assertTrue(state is CommentListState.Error)
        assertEquals("boom", (state as CommentListState.Error).message)
        assertTrue(events.any { it is CommentsEvent.ShowError })
    }
}
