package com.example.app.feature.comments.domain

interface CommentRepository {
    suspend fun getComments(limit: Int = 30): List<Comment>
}
