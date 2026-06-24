package com.example.feature.todos.data.remote

import com.example.core.network.remote.ApiRequest
import com.example.core.network.remote.RemoteDataSource
import com.example.core.network.remote.fetch
import com.example.core.utils.result.Result
import com.squareup.moshi.JsonClass
import javax.inject.Inject

@JsonClass(generateAdapter = true)
data class TodoDto(
    val id: Int,
    val todo: String = "",
    val completed: Boolean = false,
    val userId: Int = 0,
)

@JsonClass(generateAdapter = true)
data class TodosResponse(
    val todos: List<TodoDto> = emptyList(),
    val total: Int = 0,
    val skip: Int = 0,
    val limit: Int = 0,
)

interface TodoRemoteSource {
    suspend fun getTodos(limit: Int): Result<TodosResponse>
    suspend fun getTodo(id: Int): Result<TodoDto>
}

class TodoRemoteSourceImpl @Inject constructor(
    private val remote: RemoteDataSource,
) : TodoRemoteSource {

    override suspend fun getTodos(limit: Int): Result<TodosResponse> =
        remote.fetch(ApiRequest.get("todos", query = mapOf("limit" to limit.toString())))

    override suspend fun getTodo(id: Int): Result<TodoDto> =
        remote.fetch(ApiRequest.get("todos/$id"))
}
