package com.example.app.feature.users.data

import com.example.app.core.common.result.Result
import com.example.app.feature.users.data.remote.UserDto
import com.example.app.feature.users.data.remote.UserRemoteSource
import com.example.app.feature.users.domain.AppUser
import com.example.app.feature.users.domain.UserRepository
import javax.inject.Inject

private fun UserDto.toDomain() = AppUser(
    id = id, firstName = firstName, lastName = lastName, email = email,
    phone = phone, image = image, age = age, university = university,
)

private fun <T> Result<T>.getOrThrow(): T = when (this) {
    is Result.Success -> data
    is Result.Error   -> throw cause ?: IllegalStateException(message)
}

class UserRepositoryImpl @Inject constructor(
    private val remote: UserRemoteSource,
) : UserRepository {
    override suspend fun getUsers(limit: Int) = remote.getUsers(limit).getOrThrow().users.map { it.toDomain() }
    override suspend fun searchUsers(query: String) = remote.searchUsers(query).getOrThrow().users.map { it.toDomain() }
    override suspend fun getUser(id: Int) = remote.getUser(id).getOrThrow().toDomain()
}
