package com.example.app.feature.posts.domain

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
class PostHandlersTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val sample = Post(id = 1, title = "Judul", body = "Isi", tags = listOf("a"), userId = 9, views = 100, likes = 10, dislikes = 2)

    private class FakeRepository(
        private val items: List<Post> = emptyList(),
        private val error: Throwable? = null,
    ) : PostRepository {
        var searchedQuery: String? = null
        override suspend fun getPosts(limit: Int): List<Post> { error?.let { throw it }; return items }
        override suspend fun searchPosts(query: String): List<Post> { searchedQuery = query; error?.let { throw it }; return items }
        override suspend fun getPost(id: Int): Post { error?.let { throw it }; return items.first() }
    }

    @Test
    fun `load sukses menghasilkan Success`() = runTest(dispatcher) {
        val handler = PostListHandler(FakeRepository(items = listOf(sample)), backgroundScope)
        handler.load()
        advanceUntilIdle()
        assertEquals(PostListState.Success(listOf(sample)), handler.state.value)
    }

    @Test
    fun `load gagal menghasilkan Error dan ShowError`() = runTest(dispatcher) {
        val handler = PostListHandler(FakeRepository(error = IllegalStateException("boom")), backgroundScope)
        val events = mutableListOf<PostsEvent>()
        handler.events.onEach { events += it }.launchIn(backgroundScope)
        handler.load()
        advanceUntilIdle()
        val state = handler.state.value
        assertTrue(state is PostListState.Error)
        assertEquals("boom", (state as PostListState.Error).message)
        assertTrue(events.any { it is PostsEvent.ShowError })
    }
    @Test
    fun `search dengan query memanggil pencarian`() = runTest(dispatcher) {
        val repo = FakeRepository(items = listOf(sample))
        val handler = PostListHandler(repo, backgroundScope)
        handler.search("kopi")
        advanceUntilIdle()
        assertEquals("kopi", repo.searchedQuery)
        assertEquals(PostListState.Success(listOf(sample)), handler.state.value)
    }

    @Test
    fun `search dengan query kosong memuat semua`() = runTest(dispatcher) {
        val repo = FakeRepository(items = listOf(sample))
        val handler = PostListHandler(repo, backgroundScope)
        handler.search("   ")
        advanceUntilIdle()
        assertEquals(null, repo.searchedQuery)
        assertEquals(PostListState.Success(listOf(sample)), handler.state.value)
    }
    @Test
    fun `detail load sukses menghasilkan Success`() = runTest(dispatcher) {
        val handler = PostDetailHandler(FakeRepository(items = listOf(sample)), backgroundScope)
        handler.load(1)
        advanceUntilIdle()
        assertEquals(PostDetailState.Success(sample), handler.state.value)
    }

    @Test
    fun `detail load gagal menghasilkan Error`() = runTest(dispatcher) {
        val handler = PostDetailHandler(FakeRepository(error = IllegalStateException("rusak")), backgroundScope)
        handler.load(1)
        advanceUntilIdle()
        val state = handler.state.value
        assertTrue(state is PostDetailState.Error)
        assertEquals("rusak", (state as PostDetailState.Error).message)
    }
}
