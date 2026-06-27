package com.example.app.feature.posts.presentation.list

import com.example.app.feature.posts.domain.Post
import com.example.app.feature.posts.domain.PostRepository
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
class PostListViewModelTest {
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

    @Before
    fun setUp() { Dispatchers.setMain(dispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `memuat data sukses memperbarui state`() = runTest(dispatcher) {
        val vm = PostListViewModel(FakeRepository(items = listOf(sample)))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        val s = vm.uiState.value
        assertFalse(s.isLoading)
        assertEquals(1, s.posts.size)
        assertEquals(sample, s.posts.first())
        assertNull(s.errorMessage)
    }

    @Test
    fun `memuat data gagal mengisi errorMessage`() = runTest(dispatcher) {
        val vm = PostListViewModel(FakeRepository(error = IllegalStateException("boom")))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        assertEquals("boom", vm.uiState.value.errorMessage)
    }
    @Test
    fun `QueryChanged memperbarui query`() = runTest(dispatcher) {
        val vm = PostListViewModel(FakeRepository(items = listOf(sample)))
        backgroundScope.launch { vm.uiState.collect {} }
        vm.onAction(PostListAction.QueryChanged("kopi"))
        advanceUntilIdle()
        assertEquals("kopi", vm.uiState.value.query)
    }

    @Test
    fun `Submit memicu pencarian dengan query terkini`() = runTest(dispatcher) {
        val repo = FakeRepository(items = listOf(sample))
        val vm = PostListViewModel(repo)
        backgroundScope.launch { vm.uiState.collect {} }
        vm.onAction(PostListAction.QueryChanged("kopi"))
        vm.onAction(PostListAction.Submit)
        advanceUntilIdle()
        assertEquals("kopi", repo.searchedQuery)
    }
}
