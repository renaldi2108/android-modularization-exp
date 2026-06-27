package com.example.app.feature.todos.domain

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
class TodoHandlersTest {
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

    @Test
    fun `load sukses menghasilkan Success`() = runTest(dispatcher) {
        val handler = TodoListHandler(FakeRepository(items = listOf(sample)), backgroundScope)
        handler.load()
        advanceUntilIdle()
        assertEquals(TodoListState.Success(listOf(sample)), handler.state.value)
    }

    @Test
    fun `load gagal menghasilkan Error dan ShowError`() = runTest(dispatcher) {
        val handler = TodoListHandler(FakeRepository(error = IllegalStateException("boom")), backgroundScope)
        val events = mutableListOf<TodosEvent>()
        handler.events.onEach { events += it }.launchIn(backgroundScope)
        handler.load()
        advanceUntilIdle()
        val state = handler.state.value
        assertTrue(state is TodoListState.Error)
        assertEquals("boom", (state as TodoListState.Error).message)
        assertTrue(events.any { it is TodosEvent.ShowError })
    }
    @Test
    fun `detail load sukses menghasilkan Success`() = runTest(dispatcher) {
        val handler = TodoDetailHandler(FakeRepository(items = listOf(sample)), backgroundScope)
        handler.load(1)
        advanceUntilIdle()
        assertEquals(TodoDetailState.Success(sample), handler.state.value)
    }

    @Test
    fun `detail load gagal menghasilkan Error`() = runTest(dispatcher) {
        val handler = TodoDetailHandler(FakeRepository(error = IllegalStateException("rusak")), backgroundScope)
        handler.load(1)
        advanceUntilIdle()
        val state = handler.state.value
        assertTrue(state is TodoDetailState.Error)
        assertEquals("rusak", (state as TodoDetailState.Error).message)
    }
}
