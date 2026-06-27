package com.example.app.feature.todos.presentation.detail

import androidx.lifecycle.SavedStateHandle
import com.example.app.feature.todos.domain.Todo
import com.example.app.feature.todos.domain.TodoRepository
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
class TodoDetailViewModelTest {
    private val dispatcher = UnconfinedTestDispatcher()
    private val sample = Todo(id = 1, todo = "tugas", completed = true, userId = 3)

    private class FakeRepository(
        private val items: List<Todo> = emptyList(),
        private val error: Throwable? = null,
    ) : TodoRepository {
        var searchedQuery: String? = null
        override suspend fun getTodos(limit: Int): List<Todo> { error?.let { throw it }; return items }
        override suspend fun getTodo(id: Int): Todo { error?.let { throw it }; return items.first() }
    }

    private fun handle() = SavedStateHandle(mapOf("id" to "1"))

    @Before
    fun setUp() { Dispatchers.setMain(dispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `memuat detail sukses memperbarui state`() = runTest(dispatcher) {
        val vm = TodoDetailViewModel(FakeRepository(items = listOf(sample)), handle())
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        val s = vm.uiState.value
        assertFalse(s.isLoading)
        assertEquals(sample, s.todo)
    }

    @Test
    fun `memuat detail gagal mengisi errorMessage`() = runTest(dispatcher) {
        val vm = TodoDetailViewModel(FakeRepository(error = IllegalStateException("rusak")), handle())
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        assertEquals("rusak", vm.uiState.value.errorMessage)
    }
}
