package com.example.feature.todos.data

import com.example.core.utils.result.Result
import com.example.feature.todos.data.remote.TodoDto
import com.example.feature.todos.data.remote.TodoRemoteSource
import com.example.feature.todos.domain.Todo
import com.example.feature.todos.domain.TodoRepository
import javax.inject.Inject

private fun TodoDto.toDomain() = Todo(id = id, todo = todo, completed = completed, userId = userId)

private fun <T> Result<T>.getOrThrow(): T = when (this) {
    is Result.Success -> data
    is Result.Error   -> throw cause ?: IllegalStateException(message)
}

class TodoRepositoryImpl @Inject constructor(
    private val remote: TodoRemoteSource,
) : TodoRepository {
    override suspend fun getTodos(limit: Int) = remote.getTodos(limit).getOrThrow().todos.map { it.toDomain() }
    override suspend fun getTodo(id: Int) = remote.getTodo(id).getOrThrow().toDomain()
}
