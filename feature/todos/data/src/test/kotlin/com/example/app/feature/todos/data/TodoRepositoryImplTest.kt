package com.example.app.feature.todos.data

import com.example.app.core.common.result.Result
import com.example.app.feature.todos.data.remote.TodoDto
import com.example.app.feature.todos.data.remote.TodosResponse
import com.example.app.feature.todos.data.remote.TodoRemoteSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TodoRepositoryImplTest {
    private val dto = TodoDto(id = 1, todo = "tugas", completed = true, userId = 3)

    private class FakeRemote(
        var listResult: Result<TodosResponse> = Result.Success(TodosResponse()),
        var detailResult: Result<TodoDto> = Result.Success(TodoDto(id = 0)),
    ) : TodoRemoteSource {
        var searchedQuery: String? = null
        override suspend fun getTodos(limit: Int) = listResult
        override suspend fun getTodo(id: Int) = detailResult
    }

    @Test
    fun `getTodos sukses memetakan dto ke domain`() = runTest {
        val remote = FakeRemote(listResult = Result.Success(TodosResponse(todos = listOf(dto))))
        val repo = TodoRepositoryImpl(remote)
        val result = repo.getTodos()
        assertEquals(1, result.size)
        assertTrue(result[0].completed)
    }

    @Test
    fun `getTodos error melempar IllegalStateException berisi message`() = runTest {
        val repo = TodoRepositoryImpl(FakeRemote(listResult = Result.Error("gagal muat")))
        val thrown = runCatching { repo.getTodos() }.exceptionOrNull()
        assertTrue(thrown is IllegalStateException)
        assertEquals("gagal muat", thrown!!.message)
    }

    @Test
    fun `getTodos error meneruskan cause asli bila ada`() = runTest {
        val boom = IllegalArgumentException("boom")
        val repo = TodoRepositoryImpl(FakeRemote(listResult = Result.Error("gagal", cause = boom)))
        val thrown = runCatching { repo.getTodos() }.exceptionOrNull()
        assertEquals(boom, thrown)
    }
    @Test
    fun `getTodo sukses memetakan dto ke domain`() = runTest {
        val repo = TodoRepositoryImpl(FakeRemote(detailResult = Result.Success(dto)))
        val result = repo.getTodo(1)
        assertEquals(1, result.id)
        assertTrue(result.completed)
    }

    @Test
    fun `getTodo error melempar IllegalStateException`() = runTest {
        val repo = TodoRepositoryImpl(FakeRemote(detailResult = Result.Error("tidak ditemukan")))
        val thrown = runCatching { repo.getTodo(1) }.exceptionOrNull()
        assertTrue(thrown is IllegalStateException)
        assertEquals("tidak ditemukan", thrown!!.message)
    }
}
