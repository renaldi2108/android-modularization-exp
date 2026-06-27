package com.example.app.feature.auth.domain

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginWithEmail(email: String, password: String): User
    suspend fun logout()
    fun getCurrentUser(): User?
    fun observeUser(): Flow<User?>
    fun getToken(): String?
}
