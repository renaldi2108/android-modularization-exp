package com.example.feature.users.domain

interface UserRepository {
    suspend fun getUsers(limit: Int = 20): List<AppUser>
    suspend fun searchUsers(query: String): List<AppUser>
    suspend fun getUser(id: Int): AppUser
}
