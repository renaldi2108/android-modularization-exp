package com.example.feature.todos.domain

interface TodoRepository {
    suspend fun getTodos(limit: Int = 30): List<Todo>
    suspend fun getTodo(id: Int): Todo
}
