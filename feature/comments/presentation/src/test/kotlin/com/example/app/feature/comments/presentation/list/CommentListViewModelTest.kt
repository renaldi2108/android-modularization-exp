package com.example.app.feature.comments.presentation.list

import com.example.app.feature.comments.domain.Comment
import com.example.app.feature.comments.domain.CommentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CommentListViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val sample = Comment(id = 1, body = "halo", postId = 7, userName = "Eve Stone")

    private class FakeRepository(
        private val items: List<Comment> = emptyList(),
        private val error: Throwable? = null,
    ) : CommentRepository {
        var searchedQuery: String? = null
        override suspend fun getComments(limit: Int): List<Comment> { error?.let { throw it }; return items }
    }

    @Before
    fun setUp() { Dispatchers.setMain(dispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `memuat data sukses memperbarui state`() = runTest(dispatcher) {
        val vm = CommentListViewModel(FakeRepository(items = listOf(sample)))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        val s = vm.uiState.value
        assertFalse(s.isLoading)
        assertEquals(1, s.comments.size)
        assertEquals(sample, s.comments.first())
        assertNull(s.errorMessage)
    }

    @Test
    fun `memuat data gagal mengisi errorMessage`() = runTest(dispatcher) {
        val vm = CommentListViewModel(FakeRepository(error = IllegalStateException("boom")))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        assertEquals("boom", vm.uiState.value.errorMessage)
    }
}
