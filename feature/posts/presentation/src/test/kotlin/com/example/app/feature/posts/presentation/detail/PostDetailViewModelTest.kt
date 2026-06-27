package com.example.app.feature.posts.presentation.detail

import androidx.lifecycle.SavedStateHandle
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PostDetailViewModelTest {
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

    private fun handle() = SavedStateHandle(mapOf("id" to "1"))

    @Before
    fun setUp() { Dispatchers.setMain(dispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `memuat detail sukses memperbarui state`() = runTest(dispatcher) {
        val vm = PostDetailViewModel(FakeRepository(items = listOf(sample)), handle())
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        val s = vm.uiState.value
        assertFalse(s.isLoading)
        assertEquals(sample, s.post)
    }

    @Test
    fun `memuat detail gagal mengisi errorMessage`() = runTest(dispatcher) {
        val vm = PostDetailViewModel(FakeRepository(error = IllegalStateException("rusak")), handle())
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        assertEquals("rusak", vm.uiState.value.errorMessage)
    }
}
