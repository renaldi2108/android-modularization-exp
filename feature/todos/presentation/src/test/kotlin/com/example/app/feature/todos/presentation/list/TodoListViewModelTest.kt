package com.example.app.feature.todos.presentation.list

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
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TodoListViewModelTest {
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

    @Before
    fun setUp() { Dispatchers.setMain(dispatcher) }

    @After
    fun tearDown() { Dispatchers.resetMain() }

    @Test
    fun `memuat data sukses memperbarui state`() = runTest(dispatcher) {
        val vm = TodoListViewModel(FakeRepository(items = listOf(sample)))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        val s = vm.uiState.value
        assertFalse(s.isLoading)
        assertEquals(1, s.todos.size)
        assertEquals(sample, s.todos.first())
        assertNull(s.errorMessage)
    }

    @Test
    fun `memuat data gagal mengisi errorMessage`() = runTest(dispatcher) {
        val vm = TodoListViewModel(FakeRepository(error = IllegalStateException("boom")))
        backgroundScope.launch { vm.uiState.collect {} }
        advanceUntilIdle()
        assertEquals("boom", vm.uiState.value.errorMessage)
    }
}
